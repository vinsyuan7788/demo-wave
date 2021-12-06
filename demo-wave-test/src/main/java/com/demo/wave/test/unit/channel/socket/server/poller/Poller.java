package com.demo.wave.test.unit.channel.socket.server.poller;

import com.demo.wave.test.unit.channel.socket.server.utility.NeedProcessResult;
import com.demo.wave.test.unit.channel.socket.server.utility.SelectionKeyInfo;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Vince Yuan
 * @date 2021/12/6
 */
public abstract class Poller extends Thread {

    public abstract void register(SocketChannel acceptedSocketChannel);

    public ExecutorService createThreadPool() {
        return Executors.newCachedThreadPool();
    }

    public SelectionKeyInfo createSelectionKeyInfo(SelectionKey selectedKey) {
        return new SelectionKeyInfo((SocketChannel) selectedKey.channel(), selectedKey.selector(), selectedKey, selectedKey.readyOps(), selectedKey.interestOps(), selectedKey.attachment());
    }

    public NeedProcessResult needProcessReadEvent(SelectionKey selectedKey) throws Exception {
        SocketChannel acceptedSocketChannel = (SocketChannel) selectedKey.channel();
        ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
        int numberOfBytesRead = acceptedSocketChannel.read(byteBuffer);
        return numberOfBytesRead > 0 ? new NeedProcessResult(true, byteBuffer) : new NeedProcessResult(false, null);
    }

    public NeedProcessResult needProcessWriteEvent(SelectionKey selectedKey) {
        Object attachment = selectedKey.attachment();
        return attachment != null ? new NeedProcessResult(true, attachment) : new NeedProcessResult(false, null);
    }

    public NeedProcessResult needProcessWriteEvent(ConcurrentLinkedQueue<byte[]> attachmentQueue) {
        Object attachment = attachmentQueue.poll();
        return attachment != null ? new NeedProcessResult(true, attachment) : new NeedProcessResult(false, null);
    }
}
