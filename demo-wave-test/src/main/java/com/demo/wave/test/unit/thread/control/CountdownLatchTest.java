package com.demo.wave.test.unit.thread.control;

import com.demo.wave.test.unit.thread.control.resource.SharableResource;
import com.demo.wave.test.unit.thread.control.utility.latch.CountDownLatchThread;
import com.demo.wave.test.unit.thread.utility.ContainerRunner;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Vince Yuan
 * @date 2021/12/3
 */
public class CountdownLatchTest {

    private static final Logger LOG = LoggerFactory.getLogger(CountdownLatchTest.class);

    private SharableResource sharableResource = new SharableResource();
    private ContainerRunner[] threads = new ContainerRunner[sharableResource.getNumberOfThread()];

    @Before
    public void prepareTest() throws Exception {
        for (int i = 0; i < threads.length; i++) {
            String threadName = "CountDownThread" + (i + 1);
            threads[i] = new CountDownLatchThread(threadName, sharableResource);
        }
    }

    @Test
    public void performTest() throws Exception {
        LOG.info("Waiting for latch to count down to 0...");
        for (int i = 0; i < threads.length; i++) {
            threads[i].start();
        }
        sharableResource.awaitCountDownLatch();
        LOG.info("Waiting is completed!");
    }
}
