package com.demo.wave.fundamental.server.design.reactor.processor;

/**
 * @author Vince Yuan
 * @date 2021/11/30
 */
public interface BaseProcessor {

    /**
     * This method is used to handle throwable during initialization
     *
     * @param t
     */
    void handleInitializingThrowable(Throwable t);

    /**
     * This method is used to handle throwable during running
     *
     * @param t
     */
    void handleRunningThrowable(Throwable t);
}
