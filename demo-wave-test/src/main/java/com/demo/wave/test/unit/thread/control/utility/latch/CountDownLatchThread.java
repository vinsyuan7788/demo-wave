package com.demo.wave.test.unit.thread.control.utility.latch;

import com.demo.wave.test.unit.thread.control.resource.SharableResource;
import com.demo.wave.test.unit.thread.utility.ContainerRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Vince Yuan
 * @date 2021/12/3
 */
public class CountDownLatchThread extends ContainerRunner {

    private static final Logger LOG = LoggerFactory.getLogger(CountDownLatchThread.class);

    private SharableResource sharableResource;

    public CountDownLatchThread(String threadName, SharableResource sharableResource) {
        setName(threadName);
        this.sharableResource = sharableResource;
    }

    @Override
    public void run() {
        try {
            String threadName = getName();
            sharableResource.countDownLatch();
            long count = sharableResource.getCountOfLatch();
            LOG.info("{} | Count down latch: {}", threadName, count);
        } catch (Throwable t) {
            handleRunningThrowable(t);
        }
    }
}
