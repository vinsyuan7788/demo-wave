package com.demo.wave.fundamental.server.design.reactor.handler.utility;

/**
 * @author Vince Yuan
 * @date 2021/12/1
 */
public class HandlingType {

    /**
     * This field is used to signify that the socket needs to handle middle-ware
     */
    public static final int MIDDLE_WARE = 1;

    /**
     * This field is used to signify that the socket needs to handle web application
     */
    public static final int WEB_APPLICATION = 2;
}
