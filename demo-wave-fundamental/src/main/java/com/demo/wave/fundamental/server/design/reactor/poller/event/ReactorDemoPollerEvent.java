package com.demo.wave.fundamental.server.design.reactor.poller.event;

import com.demo.wave.fundamental.server.design.reactor.poller.event.utility.PollerEventHelper;
import com.demo.wave.fundamental.server.design.reactor.poller.event.utility.PollerEventType;
import com.demo.wave.fundamental.server.design.reactor.selector.utility.NioSelectionKeyInfo;
import com.demo.wave.fundamental.utility.exception.DemoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

/**
 * @author Vince Yuan
 * @date 2021/12/4
 */
public class ReactorDemoPollerEvent extends PollerEventHelper implements DemoPollerEvent {

    private static final Logger LOG = LoggerFactory.getLogger(ReactorDemoPollerEvent.class);

    private int eventType;
    private SocketChannel socketChannel;
    private Selector selector;
    private Object attachment;
    private NioSelectionKeyInfo selectionKeyInfo;

    public ReactorDemoPollerEvent(int eventType, SocketChannel socketChannel, Selector selector, Object attachment, NioSelectionKeyInfo selectionKeyInfo) {
        this.eventType = eventType;
        this.socketChannel = socketChannel;
        this.selector = selector;
        this.attachment = attachment;
        this.selectionKeyInfo = selectionKeyInfo;
    }

    @Override
    public void process() {
        try {
            int selectionKeyOpCode = PollerEventType.getSelectionKeyOpCode(eventType);
            switch (selectionKeyOpCode) {
                case SelectionKey.OP_ACCEPT:
                case SelectionKey.OP_CONNECT:
                    // Do nothing here
                    break;
                case SelectionKey.OP_READ:
                    if (selectionKeyInfo == null) {
                        socketChannel.register(selector, SelectionKey.OP_READ);
                    } else {
                        SelectionKey registeredKey = socketChannel.register(selector, SelectionKey.OP_READ);
                        int registeredOpCode = registeredKey.interestOps();
                        logSelectionKeyInfo(selectionKeyInfo.getSelectionKey(), selectionKeyInfo.getInterestOps(), registeredKey, registeredOpCode);
                    }
                    break;
                case SelectionKey.OP_WRITE:
                    SelectionKey registeredKey = socketChannel.register(selector, SelectionKey.OP_WRITE, attachment);
                    int registeredOpCode = registeredKey.interestOps();
                    logSelectionKeyInfo(selectionKeyInfo.getSelectionKey(), selectionKeyInfo.getInterestOps(), registeredKey, registeredOpCode);
                    break;
                default:
                    throw new DemoException("Selection key op code is unknown");
            }
        } catch (Throwable t) {
            handleProcessingThrowable(t);
        }
    }

    @Override
    public void handleProcessingThrowable(Throwable t) {
        LOG.error("Perform processing error", t);
    }
}
