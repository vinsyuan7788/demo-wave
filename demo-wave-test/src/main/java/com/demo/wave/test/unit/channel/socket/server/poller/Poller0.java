package com.demo.wave.test.unit.channel.socket.server.poller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Vince Yuan
 * @date 2021/12/6
 */
public class Poller0 extends Poller {

    private static final Logger LOG = LoggerFactory.getLogger(Poller0.class);

    private Selector selector;

    public Poller0() {
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
                selector.select(1000);
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
                        SocketChannel acceptedSocketChannel = (SocketChannel) selectedKey.channel();
                        Selector selector = selectedKey.selector();
                        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
                        int numberOfBytesRead = acceptedSocketChannel.read(byteBuffer);
                        if (numberOfBytesRead <= 0) { return; }
                        byte[] byteArray = byteBuffer.array();
                        LOG.info("Server reads data: {}", new String(byteArray));
                        acceptedSocketChannel.register(selector, SelectionKey.OP_WRITE, byteArray);
                    }
                    if (selectedKey.isWritable()) {
                        SocketChannel acceptedSocketChannel = (SocketChannel) selectedKey.channel();
                        Selector selector = selectedKey.selector();
                        byte[] byteArray = (byte[]) selectedKey.attachment();
                        ByteBuffer byteBuffer = ByteBuffer.wrap(byteArray);
                        int numberOfBytesWritten = acceptedSocketChannel.write(byteBuffer);
                        if (numberOfBytesWritten <= 0) { return; }
                        acceptedSocketChannel.register(selector, SelectionKey.OP_READ);
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
}
