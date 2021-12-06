package com.demo.wave.test.unit.channel.socket.server.utility;

import java.nio.ByteBuffer;

/**
 * @author Vince Yuan
 * @date 2021/12/7
 */
public class NeedProcessResult {

    private boolean needProcess;
    private ByteBuffer byteBufferWithReadData;
    private Object attachment;

    public NeedProcessResult(boolean needProcess, ByteBuffer byteBufferWithReadData) {
        this.needProcess = needProcess;
        this.byteBufferWithReadData = byteBufferWithReadData;
    }

    public NeedProcessResult(boolean needProcess, Object attachment) {
        this.needProcess = needProcess;
        this.attachment = attachment;
    }

    public boolean isNeedProcess() {
        return needProcess;
    }

    public ByteBuffer getByteBufferWithReadData() {
        return byteBufferWithReadData;
    }

    public Object getAttachment() {
        return attachment;
    }
}
