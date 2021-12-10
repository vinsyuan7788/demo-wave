package com.demo.wave.test.unit.thread.data.utility.lock;

import com.demo.wave.test.unit.thread.data.resource.SharableResource;
import com.demo.wave.test.unit.thread.utility.AccessWriteContentResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Callable;

/**
 * @author Vince Yuan
 * @date 2021/12/10
 */
public class AccessWriteContentThread implements Callable<AccessWriteContentResult> {

    private static final Logger LOG = LoggerFactory.getLogger(AccessWriteContentThread.class);

    private String threadName;
    private SharableResource sharableResource;

    public AccessWriteContentThread(String threadName, SharableResource sharableResource) {
        this.threadName = threadName;
        this.sharableResource = sharableResource;
    }

    @Override
    public AccessWriteContentResult call() throws Exception {
        LOG.info("{} | Accessing write content", threadName);
        String content = sharableResource.accessWriteContentWithLockAndCondition();
        LOG.info("{} | Access write content successfully: {}", threadName, content);
        return AccessWriteContentResult.create(threadName, content);
    }
}
