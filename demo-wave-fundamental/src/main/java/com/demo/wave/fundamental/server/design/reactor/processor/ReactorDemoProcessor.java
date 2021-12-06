package com.demo.wave.fundamental.server.design.reactor.processor;

import com.demo.wave.fundamental.server.design.reactor.socket.DemoSocketWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Vince Yuan
 * @date 2021/11/30
 */
public class ReactorDemoProcessor implements Runnable, DemoProcessor {

    private static final Logger LOG = LoggerFactory.getLogger(ReactorDemoProcessor.class);

    private DemoSocketWrapper socketWrapper;

    public ReactorDemoProcessor(DemoSocketWrapper socketWrapper) {
        try {
            this.socketWrapper = socketWrapper;
        } catch (Throwable t) {
            handleInitializingThrowable(t);
        }
    }

    @Override
    public void run() {
        try {
            socketWrapper.process();
        } catch (Throwable t) {
            handleRunningThrowable(t);
        }
    }

    @Override
    public void handleInitializingThrowable(Throwable t) {
        LOG.error("Create processor error", t);
    }

    @Override
    public void handleRunningThrowable(Throwable t) {
        LOG.error("Running processor error", t);
    }
}
