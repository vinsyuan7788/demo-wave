package com.demo.wave.test.unit.thread.data.utility.lock;

import com.demo.wave.test.unit.thread.data.resource.SharableResource;
import com.demo.wave.test.unit.thread.utility.ContainerRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Vince Yuan
 * @date 2021/12/10
 */
public class CountUpThread extends ContainerRunner {

    private static final Logger LOG = LoggerFactory.getLogger(CountUpThread.class);

    private SharableResource sharableResource;

    public CountUpThread(String threadName, SharableResource sharableResource) {
        setName(threadName);
        this.sharableResource = sharableResource;
    }

    @Override
    public void run() {
        try {
            String threadName = getName();
            // Count up per 100 millis for 100 round, hence max count would be 10000 on 10 seconds
            for (int i = 0; i < 100; i++) {
                Long currentCount = sharableResource.countUpWithLock();
                LOG.info("{} | Current count: {}", threadName, currentCount);
                sleep(100);
            }
        } catch (Throwable t) {
            handleRunningThrowable(t);
        }
    }
}
