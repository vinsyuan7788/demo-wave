package com.demo.wave.fundamental.client.nonblocking.socket;

/**
 * @author Vince Yuan
 * @date 2021/11/20
 */
public interface BaseClientSocket {

    /************************ Setup and Shutdown ************************/

    /**
     * This method is used to start the socket
     */
    void startSocket();

    /**
     * This method is used to close the socket <br/>
     */
    void closeSocket();

    /**
     * This method is used to handle throwable during initializing. <br/>
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
     * This method is used handle throwable during closing
     *
     * @param t
     */
    void handleClosingThrowable(Throwable t);

    /************************ Data Processing ************************/

    /**
     * This method is used handle throwable during closing. <br/>
     *
     * @param t
     */
    void handleRunningThrowable(Throwable t);
}
