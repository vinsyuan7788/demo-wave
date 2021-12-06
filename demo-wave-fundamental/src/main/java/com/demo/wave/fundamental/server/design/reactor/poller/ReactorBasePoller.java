package com.demo.wave.fundamental.server.design.reactor.poller;

import com.demo.wave.fundamental.server.design.reactor.poller.utility.NeedProcessEvent;
import com.demo.wave.fundamental.server.design.reactor.processor.ReactorBaseProcessor;
import com.demo.wave.fundamental.server.design.reactor.selector.utility.NioSelectionKeyInfo;
import com.demo.wave.fundamental.server.design.reactor.socket.BaseSocketWrapper;
import com.demo.wave.fundamental.server.design.reactor.socket.ReactorBaseSocketWrapper;
import com.demo.wave.fundamental.utility.auxiliary.ContainerRunner;
import com.demo.wave.fundamental.utility.state.server.reactor.PollerState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.Executor;

/**
 * @author Vince Yuan
 * @date 2021/11/30
 */
public class ReactorBasePoller extends ContainerRunner implements Runnable, BasePoller {

    private static final Logger LOG = LoggerFactory.getLogger(ReactorBasePoller.class);

    private String pollerName;
    private int processingType;
    private ContainerRunner pollerThread;
    private Executor threadPool;

    private Selector selector;

    private PollerState pollerState;

    public ReactorBasePoller(String pollerName, int processingType) {
        try {
            this.pollerName = pollerName;
            this.processingType = processingType;
            pollerThread = new ContainerRunner(this, pollerName);
            threadPool = getDefaultCachedThreadPool("ProcessorThreadPool-In-" + pollerName);

            selector = Selector.open();
            LOG.info("{} | Selector is opened: {}", pollerName, selector);

            setPollerState(PollerState.INITIAL);
        } catch (Throwable t) {
            handleInitializingThrowable(t);
        }
    }

    @Override
    public void run() {
        setPollerState(PollerState.RUNNING);
        while (pollerState == PollerState.RUNNING) {
            try {
                selectKeys(selector);
                Set<SelectionKey> selectedKeys = getSelectedKeys(selector);
                processSelectedKeys(selectedKeys);
            } catch (Throwable t) {
                handleRunningThrowable(t);
            }
            roundOfSelect.incrementAndGet();
        }
    }

    @Override
    public void register(SocketChannel acceptedSocketChannel) {
        try {
            SelectionKey registeredKey = acceptedSocketChannel.register(selector, SelectionKey.OP_READ);
            LOG.info("{} | Accepted socket channel is registered to selector with interest of read event | registered key: {}", pollerName, registeredKey);
        } catch (Throwable t) {
            handleRegisteringThrowable(t);
        }
    }

    @Override
    public int selectKeys(Selector selector) throws Exception {
        return selector.selectNow();
    }

    @Override
    public Set<SelectionKey> getSelectedKeys(Selector selector) {
        return selector.selectedKeys();
    }

    @Override
    public void processSelectedKeys(Set<SelectionKey> selectedKeys) throws Exception {
        Iterator<SelectionKey> selectedKeyIterator = selectedKeys.iterator();
        roundOfIteratingSelectedKeys.set(1);
        while (selectedKeyIterator.hasNext()) {
            SelectionKey selectedKey = selectedKeyIterator.next();
            if (selectedKey.isAcceptable()) {
                processAcceptEvent(selectedKey);
            }
            if (selectedKey.isConnectable()) {
                processConnectEvent(selectedKey);
            }
            if (selectedKey.isReadable()) {
                processReadEvent(selectedKey);
            }
            if (selectedKey.isWritable()) {
                processWriteEvent(selectedKey);
            }
            selectedKeyIterator.remove();
            roundOfIteratingSelectedKeys.incrementAndGet();
        }
        roundOfIteratingSelectedKeys.set(1);
    }

    @Override
    public void startPoller() {
        try {
            pollerThread.start();
        } catch (Throwable t) {
            handleStartingThrowable(t);
        }
    }

    @Override
    public void closePoller() {
        try {
            setPollerState(PollerState.CLOSED);
            pollerThread.join();
            if (selector.isOpen()) {
                selector.close();
            }
        } catch (Throwable t) {
            handleClosingThrowable(t);
        }
    }

    @Override
    public void handleInitializingThrowable(Throwable t) {
        LOG.info("Create poller error", t);
    }

    @Override
    public void handleStartingThrowable(Throwable t) {
        LOG.info("Start poller error", t);
    }

    @Override
    public void handleClosingThrowable(Throwable t) {
        LOG.info("Close poller error", t);
    }

    @Override
    public void handleRegisteringThrowable(Throwable t) {
        LOG.info("Register to poller error", t);
    }

    @Override
    public void handleRunningThrowable(Throwable t) {
        LOG.info("Running poller error", t);
    }

    @Override
    public void processAcceptEvent(SelectionKey selectedKey) throws Exception {
        // Do nothing here
    }

    @Override
    public void processConnectEvent(SelectionKey selectedKey) throws Exception {
        // Do nothing here
    }

    @Override
    public void processReadEvent(SelectionKey selectedKey) throws Exception {
        NeedProcessEvent needProcessEvent = needProcessReadEvent(selectedKey);
        if (!needProcessEvent.isProcessNeeded()) { return; }
        NioSelectionKeyInfo selectedKeyInfo = getSelectedKeyInfoBeforeCancel(selectedKey);
        BaseSocketWrapper socketWrapper = new ReactorBaseSocketWrapper(processingType, selectedKeyInfo, this, needProcessEvent);
        ReactorBaseProcessor socketProcessor = new ReactorBaseProcessor(socketWrapper);
        threadPool.execute(socketProcessor);
    }

    @Override
    public void processWriteEvent(SelectionKey selectedKey) throws Exception {
        // Do nothing here
    }

    @Override
    public NeedProcessEvent needProcessReadEvent(SelectionKey selectedKey) throws Exception {
        SocketChannel acceptedSocketChannel = (SocketChannel) selectedKey.channel();
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(1024);
        int numberOfBytesRead = acceptedSocketChannel.read(byteBuffer);
        return numberOfBytesRead > 0 ? NeedProcessEvent.create(true, numberOfBytesRead, byteBuffer) : NeedProcessEvent.create(false);
    }

    /********************************* State Machine *********************************/

    /**
     * This method is used to set the poller state
     *
     * @param pollerState
     */
    private void setPollerState(PollerState pollerState) {
        this.pollerState = pollerState;
        LOG.info("{} | Poller state is set to {}", pollerName, pollerState);
    }

    public PollerState getPollerState() {
        return pollerState;
    }

    /********************************* Utility Method *********************************/

    /**
     * This method is used to get the selected key information <br/>
     * This method should be invoked before {@link SelectionKey#cancel()} is called.
     *
     * @param selectedKey
     * @return
     */
    private NioSelectionKeyInfo getSelectedKeyInfoBeforeCancel(SelectionKey selectedKey) {
        return NioSelectionKeyInfo.create(selectedKey, selectedKey.channel(), selectedKey.selector(), selectedKey.attachment(), selectedKey.interestOps(), selectedKey.readyOps());
    }
}
