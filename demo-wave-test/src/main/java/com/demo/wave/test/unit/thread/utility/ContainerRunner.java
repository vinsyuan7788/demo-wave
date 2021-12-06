package com.demo.wave.test.unit.thread.utility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Vince Yuan
 * @date 2021/12/3
 */
public class ContainerRunner extends Thread {

    private static final Logger LOG = LoggerFactory.getLogger(ContainerRunner.class);

    private final String className = getClass().getSimpleName();

    public void handleRunningThrowable(Throwable t) {
        LOG.error(className + " | Error occurs during running", t);
    }
}
