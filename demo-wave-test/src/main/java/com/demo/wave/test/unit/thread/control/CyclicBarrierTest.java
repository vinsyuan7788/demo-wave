package com.demo.wave.test.unit.thread.control;

import com.demo.wave.test.unit.thread.control.resource.SharableResource;
import com.demo.wave.test.unit.thread.control.utility.barrier.CyclicBarrierThread;
import com.demo.wave.test.unit.thread.utility.ContainerRunner;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Vince Yuan
 * @date 2021/12/3
 */
public class CyclicBarrierTest {

    private static final Logger LOG = LoggerFactory.getLogger(CyclicBarrierTest.class);

    private SharableResource sharableResource = new SharableResource();
    private ContainerRunner[] threads = new ContainerRunner[sharableResource.getNumberOfThread()];

    @Before
    public void prepareTest() throws Exception {
        for (int i = 0; i < threads.length; i++) {
            String threadName = "CyclicBarrierThread" + (i + 1);
            threads[i] = new CyclicBarrierThread(threadName, sharableResource);
        }
    }

    @Test
    public void performTest() throws Exception {
        LOG.info("Waiting until the barrier reaches given parties...");
        for (int i = 0; i < threads.length; i++) {
            threads[i].start();
        }
        sharableResource.awaitCyclicBarrier();
        LOG.info("Waiting is completed!");
    }
}
