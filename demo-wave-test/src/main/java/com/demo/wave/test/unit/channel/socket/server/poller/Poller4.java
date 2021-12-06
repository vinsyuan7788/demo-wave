package com.demo.wave.test.unit.channel.socket.server.poller;

import com.demo.wave.test.unit.channel.socket.server.processor.Processor4;
import com.demo.wave.test.unit.channel.socket.server.utility.NeedProcessResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Vince Yuan
 * @date 2021/12/6
 */
public class Poller4 extends Poller {

    private static final Logger LOG = LoggerFactory.getLogger(Poller4.class);

    private Selector selector;
    private ExecutorService threadPool = createThreadPool();
    private ConcurrentLinkedQueue<byte[]> attachmentQueue = new ConcurrentLinkedQueue<>();

    public Poller4() {
        try {
            selector = Selector.open();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
    @Override
    public void run() {
        AtomicLong round = new AtomicLong(1);
        while (true) {
            try {
                selector.selectNow();
                Set<SelectionKey> selectedKeySet = selector.selectedKeys();
                Iterator<SelectionKey> selectedKeyIterator = selectedKeySet.iterator();
                while (selectedKeyIterator.hasNext()) {
                    SelectionKey selectedKey = selectedKeyIterator.next();
                    if (selectedKey.isAcceptable()) {
                        // Do nothing here
                    }
                    if (selectedKey.isConnectable()) {
                        // Do nothing here
                    }
                    if (selectedKey.isReadable()) {
                        NeedProcessResult needProcessResult = needProcessReadEvent(selectedKey);
                        if (!needProcessResult.isNeedProcess()) { continue; }
                        threadPool.execute(new Processor4("processor-" + round.get(), "read", createSelectionKeyInfo(selectedKey), this, needProcessResult));
                    }
                    if (selectedKey.isWritable()) {
                        NeedProcessResult needProcessResult = needProcessWriteEvent(attachmentQueue);
                        if (!needProcessResult.isNeedProcess()) { continue; }
                        threadPool.execute(new Processor4("processor-" + round.get(), "write", createSelectionKeyInfo(selectedKey), this, needProcessResult));
                    }
                    selectedKeyIterator.remove();
                }
            } catch (Throwable t) {
                t.printStackTrace();
            }
            round.incrementAndGet();
        }
    }

    @Override
    public void register(SocketChannel acceptedSocketChannel) {
        try {
            acceptedSocketChannel.register(selector, SelectionKey.OP_READ);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public Selector getSelector() {
        return selector;
    }

    public ExecutorService getThreadPool() {
        return threadPool;
    }

    public ConcurrentLinkedQueue<byte[]> getAttachmentQueue() {
        return attachmentQueue;
    }
}
