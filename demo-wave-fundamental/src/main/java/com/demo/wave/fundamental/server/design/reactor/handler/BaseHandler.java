package com.demo.wave.fundamental.server.design.reactor.handler;

/**
 * @author Vince Yuan
 * @date 2021/12/1
 */
public interface BaseHandler {

    /**
     * This method is used to handle throwable during initialization
     *
     * @param t
     */
    void handleInitializingThrowable(Throwable t);

    /**
     * This method is used to process the socket
     */
    void process();

    /**
     * This method is used to handle throwable during processing
     *
     * @param t
     */
    void handleProcessingThrowable(Throwable t);
}
