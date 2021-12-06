package com.demo.wave.fundamental.server.design.reactor.socket;

import com.demo.wave.fundamental.server.design.reactor.poller.BasePoller;
import com.demo.wave.fundamental.server.design.reactor.poller.utility.NeedProcessEvent;
import com.demo.wave.fundamental.server.design.reactor.selector.utility.NioSelectionKeyInfo;

/**
 * @author Vince Yuan
 * @date 2021/12/2
 */
public interface BaseSocketWrapper {

    /**
     * This method is used to process the socket wrapper
     */
    void process();

    /**
     * This method is used to handle throwable during processing
     *
     * @param t
     */
    void handleProcessingThrowable(Throwable t);

    /**
     * This method is used to get selection key information
     *
     * @return
     */
    NioSelectionKeyInfo getSelectionKeyInfo();

    /**
     * This method is used to get the poller
     *
     * @return
     */
    BasePoller getPoller();

    /**
     * This method is used to get the result representing if the event needs to be processed
     *
     * @return
     */
    NeedProcessEvent getNeedProcessEvent();
}
