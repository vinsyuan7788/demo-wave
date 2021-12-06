package com.demo.wave.fundamental.client.nonblocking;

/**
 * @author Vince Yuan
 * @date 2021/11/20
 */
public abstract class BaseClient {

    /**
     * This method is used to start the client
     */
    public abstract void startClient();

    /**
     * This method is used to close the client
     */
    public abstract void closeClient();
}
