package com.demo.wave.fundamental.server.design.reactor.acceptor;

import java.nio.channels.SocketChannel;

/**
 * @author Vince Yuan
 * @date 2021/11/29
 */
public interface DemoAcceptor {

    /**
     * This method is used to configure the socket channel. <br/>
     * It can be used to configure the accepted socket channel before registering it to the poller,
     * which is important since the poller works in NIO mode, and the socket channel needs to be
     * properly configured before registering it to the poller
     *
     * @param socketChannel
     */
    void configureSocketChannel(SocketChannel socketChannel);

    /**
     * This method is used to handle throwable during configuring socket channel
     *
     * @param t
     */
    void handleConfiguringSocketChannelThrowable(Throwable t);

    /**
     * This method is used to start the acceptor
     */
    void startAcceptor();

    /**
     * This method is used to close the acceptor
     */
    void closeAcceptor();

    /**
     * This method is used to handle throwable during initialization
     *
     * @param t
     */
    void handleInitializingThrowable(Throwable t);

    /**
     * This method is used to handle throwable during starting
     *
     * @param t
     */
    void handleStartingThrowable(Throwable t);

    /**
     * This method is used to handle throwable during closing
     *
     * @param t
     */
    void handleClosingThrowable(Throwable t);

    /**
     * This method id used to handle throwable during running
     *
     * @param t
     */
    void handleRunningThrowable(Throwable t);
}
