package com.demo.wave.fundamental.server.design.reactor.poller.utility;

import java.nio.ByteBuffer;

/**
 * @author Vince Yuan
 * @date 2021/12/7
 */
public class NeedProcessEvent {

    private boolean processNeeded;
    private int numberOfBytesRead;
    private ByteBuffer byteBufferWithDataRead;
    private Object attachmentForDataWrite;

    private NeedProcessEvent(boolean processNeeded, int numberOfBytesRead, ByteBuffer byteBufferWithDataRead) {
        this.processNeeded = processNeeded;
        this.numberOfBytesRead = numberOfBytesRead;
        this.byteBufferWithDataRead = byteBufferWithDataRead;
    }

    private NeedProcessEvent(boolean processNeeded, Object attachmentForDataWrite) {
        this.processNeeded = processNeeded;
        this.attachmentForDataWrite = attachmentForDataWrite;
    }

    private NeedProcessEvent(boolean processNeeded) {
        this.processNeeded = processNeeded;
    }

    public static NeedProcessEvent create(boolean processNeeded, int numberOfBytesRead, ByteBuffer byteBufferWithDataRead) {
        return new NeedProcessEvent(processNeeded, numberOfBytesRead, byteBufferWithDataRead);
    }

    public static NeedProcessEvent create(boolean processNeeded, Object attachmentForDataWrite) {
        return new NeedProcessEvent(processNeeded, attachmentForDataWrite);
    }

    public static NeedProcessEvent create(boolean processNeeded) {
        return new NeedProcessEvent(processNeeded);
    }

    public boolean isProcessNeeded() {
        return processNeeded;
    }

    public int getNumberOfBytesRead() {
        return numberOfBytesRead;
    }

    public ByteBuffer getByteBufferWithDataRead() {
        return byteBufferWithDataRead;
    }

    public Object getAttachmentForDataWrite() {
        return attachmentForDataWrite;
    }
}
