package com.demo.wave.test.unit.thread.utility;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Vince Yuan
 * @date 2021/12/11
 */
public class TestHelper {

    /**
     * This method is used to get a thread pool
     *
     * @return
     */
    public ExecutorService getThreadPool() {
        return Executors.newCachedThreadPool();
    }
}
