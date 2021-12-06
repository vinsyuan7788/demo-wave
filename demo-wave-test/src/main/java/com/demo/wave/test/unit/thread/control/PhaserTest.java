package com.demo.wave.test.unit.thread.control;

import com.demo.wave.test.unit.thread.control.resource.SharableResource;
import com.demo.wave.test.unit.thread.utility.ContainerRunner;
import com.demo.wave.test.unit.thread.control.utility.phaser.PhaserAsCountDownLatchThread;
import com.demo.wave.test.unit.thread.control.utility.phaser.PhaserAsCyclicBarrierThread;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Vince Yuan
 * @date 2021/12/3
 */
public class PhaserTest {

    private static final Logger LOG = LoggerFactory.getLogger(PhaserTest.class);

    private SharableResource sharableResource = new SharableResource();
    private ContainerRunner[] threads1 = new ContainerRunner[sharableResource.getNumberOfThread()];
    private ContainerRunner[] threads2 = new ContainerRunner[sharableResource.getNumberOfThread()];

    @Before
    public void prepareTest() throws Exception {
        for (int i = 0; i < threads1.length; i++) {
            String threadName = "PhaserAsCountDownLatchThread" + (i + 1);
            threads1[i] = new PhaserAsCountDownLatchThread(threadName, sharableResource);
        }
        for (int i = 0; i < threads2.length; i++) {
            String threadName = "PhaserAsCyclicBarrierThread" + (i + 1);
            threads2[i] = new PhaserAsCyclicBarrierThread(threadName, sharableResource);
        }
    }

    @Test
    public void performTest1() throws Exception {
        LOG.info("Waiting until the phaser unregisters given parties...");
        for (int i = 0; i < threads1.length; i++) {
            threads1[i].start();
        }
        sharableResource.arrivePhaserAndAwaitAdvance(sharableResource.getPhaserAsCountDownLatch());
        LOG.info("Waiting is completed...");
    }

    @Test
    public void performTest2() throws Exception {
        LOG.info("Waiting until the phaser reaches given parties...");
        for (int i = 0; i < threads2.length; i++) {
            threads2[i].start();
        }
        sharableResource.arrivePhaserAndAwaitAdvance(sharableResource.getPhaserAsCyclicBarrier());
        LOG.info("Waiting is completed...");
    }
}
