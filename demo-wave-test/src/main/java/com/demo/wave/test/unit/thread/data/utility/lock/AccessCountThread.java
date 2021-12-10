package com.demo.wave.test.unit.thread.data.utility.lock;

import com.demo.wave.test.unit.thread.data.resource.SharableResource;
import com.demo.wave.test.unit.thread.utility.AccessCountResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;

/**
 * @author Vince Yuan
 * @date 2021/12/10
 */
public class AccessCountThread implements Callable<AccessCountResult> {

    private static final Logger LOG = LoggerFactory.getLogger(AccessCountThread.class);

    private String threadName;
    private SharableResource sharableResource;

    public AccessCountThread(String threadName, SharableResource sharableResource) {
        this.threadName = threadName;
        this.sharableResource = sharableResource;
    }

    @Override
    public AccessCountResult call() throws Exception {
        LOG.info("{} | Accessing count", threadName);
        Long count = sharableResource.accessCountWithLockAndCondition();
        LOG.info("{} | Access count successfully: {}", threadName, count);
        return AccessCountResult.create(threadName, count);
    }
}
