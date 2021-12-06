package com.demo.wave.fundamental.server.business.common.processor;

import com.demo.wave.fundamental.server.business.middleware.utility.data.network.packet.Packet;

/**
 * @author Vince Yuan
 * @date 2021/12/1
 */
public interface BaseProcessor {

    /**
     * This method is used to handle throwable during initialization
     *
     * @param t
     */
    void handleInitializingThrowable(Throwable t);

    /**
     * This method is used to process socket
     */
    void process();

    /**
     * This method is used to handle throwable during processing
     *
     * @param t
     */
    void handleProcessingThrowable(Throwable t);

    /**
     * This method is used to process read event
     *
     * @throws Exception
     */
    void processReadEvent() throws Exception;

    /**
     * This method is used to process the received packet and return a new one
     *
     * @param receivedPacket
     * @return
     */
    Packet processReceivedPacket(Packet receivedPacket);
}
