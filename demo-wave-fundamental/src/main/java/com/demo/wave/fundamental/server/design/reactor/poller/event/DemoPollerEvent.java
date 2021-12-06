package com.demo.wave.fundamental.server.design.reactor.poller.event;

/**
 * @author Vince Yuan
 * @date 2021/12/4
 */
public interface DemoPollerEvent {

    /**
     * This method is used to process the event
     */
    void process();

    /**
     * This method is used to handle throwable during processing
     *
     * @param t
     */
    void handleProcessingThrowable(Throwable t);
}
