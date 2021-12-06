package com.demo.wave.fundamental.server.design.reactor.selector;

import com.demo.wave.fundamental.utility.exception.DemoException;
import com.demo.wave.fundamental.server.design.reactor.selector.utility.NioSelectorPoolMode;

import java.nio.channels.Selector;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author Vince Yuan
 * @date 2021/11/30
 */
public class NioSelectorPool {

    private int selectorPoolModel;
    private volatile Selector sharedSelector;
    private ConcurrentLinkedQueue<Selector> selectorQueue;

    public NioSelectorPool(int selectorPoolModel) {
        this.selectorPoolModel = selectorPoolModel;
        selectorQueue = new ConcurrentLinkedQueue<>();
    }

    public Selector getSelector() {
        Selector selector;
        switch (selectorPoolModel) {
            case NioSelectorPoolMode.SHARED:
                selector = getSharedSelector();
                break;
            case NioSelectorPoolMode.ROUND_BIN:
                selector = getRoundBinSelector();
                break;
            default:
                throw new DemoException("Selector pool mode is unknown");
        }
        return selector;
    }

    private Selector getSharedSelector() {
        if (sharedSelector == null) {
            synchronized (Selector.class) {
                if (sharedSelector == null) {
                    try {
                        sharedSelector = Selector.open();
                    } catch (Throwable t) {
                        throw new DemoException("Open selector error", t);
                    }
                }
            }
        }
        return sharedSelector;
    }

    private Selector getRoundBinSelector() {
        return selectorQueue.poll();
    }
}
