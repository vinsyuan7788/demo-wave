package com.demo.wave.test.unit.channel.socket.server.processor;

import com.demo.wave.test.unit.channel.socket.server.poller.Poller4;
import com.demo.wave.test.unit.channel.socket.server.utility.NeedProcessResult;
import com.demo.wave.test.unit.channel.socket.server.utility.SelectionKeyInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author Vince Yuan
 * @date 2021/12/6
 */
public class Processor4 extends Processor {

    private static final Logger LOG = LoggerFactory.getLogger(Processor4.class);

    private String processorName;
    private String mode;
    private SelectionKeyInfo selectedKeyInfo;
    private Poller4 poller;
    private NeedProcessResult needProcessResult;

    public Processor4(String processorName, String mode, SelectionKeyInfo selectedKeyInfo, Poller4 poller, NeedProcessResult needProcessResult) {
        this.processorName = processorName;
        this.mode = mode;
        this.selectedKeyInfo = selectedKeyInfo;
        this.poller = poller;
        this.needProcessResult = needProcessResult;
    }

    @Override
    public void run() {
        try {
            logModeAndEventIfDifferent(mode, selectedKeyInfo.getReadyOps());
            SocketChannel acceptedSocketChannel = selectedKeyInfo.getAcceptedSocketChannel();
            Selector selector = selectedKeyInfo.getSelector();
            ConcurrentLinkedQueue<byte[]> attachmentQueue = poller.getAttachmentQueue();
            if ("read".equals(mode)) {
                logModeAndEventIfDifferent(mode, selectedKeyInfo.getReadyOps());
//                ByteBuffer byteBuffer = ByteBuffer.allocate(1024);
//                int numberOfBytesRead = acceptedSocketChannel.read(byteBuffer);
//                if (numberOfBytesRead <= 0) { return; }
                ByteBuffer byteBuffer = needProcessResult.getByteBufferWithReadData();
                byte[] byteArray = byteBuffer.array();
                LOG.info("{} | {} | Server reads data: {}", processorName, getName(), new String(byteArray));
                attachmentQueue.offer(byteArray);
                acceptedSocketChannel.register(selector, SelectionKey.OP_WRITE | SelectionKey.OP_READ);
            }
            if ("write".equals(mode)) {
                logModeAndEventIfDifferent(mode, selectedKeyInfo.getReadyOps());
//                byte[] byteArray = attachmentQueue.poll();
//                if (byteArray == null) { return; }
                byte[] byteArray = (byte[]) needProcessResult.getAttachment();
                ByteBuffer byteBuffer = ByteBuffer.wrap(byteArray);
                int numberOfBytesWritten = acceptedSocketChannel.write(byteBuffer);
                if (numberOfBytesWritten <= 0) { return; }
                acceptedSocketChannel.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}
