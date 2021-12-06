package com.demo.wave.fundamental.utility.executor;

import java.util.concurrent.ExecutorService;

/**
 * @author Vince Yuan
 * @date 2021/11/30
 */
public interface ThreadPoolProvider {

    /**
     * This method is used to get a default cached thread pool implemented by executors
     *
     * @param threadName
     * @return
     */
    ExecutorService getDefaultCachedThreadPool(String threadName);
}
