package com.demo.wave.test.unit.thread.utility;

/**
 * @author Vince Yuan
 * @date 2021/12/10
 */
public class AccessReadContentResult {

    private String threadName;
    private String readContent;

    private AccessReadContentResult(String threadName, String readContent) {
        this.threadName = threadName;
        this.readContent = readContent;
    }

    public String getThreadName() {
        return threadName;
    }

    public String getReadContent() {
        return readContent;
    }

    public static AccessReadContentResult create(String threadName, String readContent) {
        return new AccessReadContentResult(threadName, readContent);
    }
}
