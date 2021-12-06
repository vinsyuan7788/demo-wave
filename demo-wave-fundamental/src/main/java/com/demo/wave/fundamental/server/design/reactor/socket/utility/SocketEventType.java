package com.demo.wave.fundamental.server.design.reactor.socket.utility;

/**
 * This class is used to define the socket event to be processed by business
 * processor eventually. <br/>
 * There may be multiple events ready to be processed (based on selected key),
 * but the event still needs to be processed case-by-case, which is what this
 * class is for.
 *
 * @author Vince Yuan
 * @date 2021/12/5
 */
public class SocketEventType {

    /**
     * This filed is used to indicate the socket event to be processed eventually.

     */
    public static final int READ = 1;

    /**
     *
     */
    public static final int WRITE = 2;
}
