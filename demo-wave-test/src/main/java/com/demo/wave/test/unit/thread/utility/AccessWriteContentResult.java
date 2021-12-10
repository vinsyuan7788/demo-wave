package com.demo.wave.test.unit.thread.utility;

/**
 * @author Vince Yuan
 * @date 2021/12/10
 */
public class AccessWriteContentResult {

    private String threadName;
    private String writeContent;

    private AccessWriteContentResult(String threadName, String writeContent) {
        this.threadName = threadName;
        this.writeContent = writeContent;
    }

    public String getThreadName() {
        return threadName;
    }

    public String getWriteContent() {
        return writeContent;
    }

    public static AccessWriteContentResult create(String threadName, String readContent) {
        return new AccessWriteContentResult(threadName, readContent);
    }
}
