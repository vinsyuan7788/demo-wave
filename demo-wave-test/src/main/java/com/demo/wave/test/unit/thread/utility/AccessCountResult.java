package com.demo.wave.test.unit.thread.utility;

/**
 * @author Vince Yuan
 * @date 2021/12/10
 */
public class AccessCountResult {

    private String threadName;
    private Long count;

    private AccessCountResult(String threadName, Long count) {
        this.threadName = threadName;
        this.count = count;
    }

    public String getThreadName() {
        return threadName;
    }

    public Long getCount() {
        return count;
    }

    public static AccessCountResult create(String threadName, Long count) {
        return new AccessCountResult(threadName, count);
    }
}
