package com.demo.wave.fundamental.server.design.reactor.selector.utility;

/**
 * @author Vince Yuan
 * @date 2021/11/30
 */
public class NioSelectorPoolMode {

    /**
     * This field is used to signify that selector pool always return
     * a shared selector;
     */
    public static final int SHARED = 1;

    /**
     * This field is used to signify that selector pool returns a selector
     * in a round-bin way
     */
    public static final int ROUND_BIN = 2;
}
