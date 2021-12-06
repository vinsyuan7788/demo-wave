package com.demo.wave.test.unit.thread.control.utility.barrier;

import com.demo.wave.test.unit.thread.control.resource.SharableResource;
import com.demo.wave.test.unit.thread.utility.ContainerRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Vince Yuan
 * @date 2021/12/3
 */
public class CyclicBarrierThread extends ContainerRunner {

    private static final Logger LOG = LoggerFactory.getLogger(CyclicBarrierThread.class);

    private SharableResource sharableResource;

    public CyclicBarrierThread(String threadName, SharableResource sharableResource) {
        setName(threadName);
        this.sharableResource = sharableResource;
    }

    @Override
    public void run() {
        try {
            String threadName = getName();
            int numberOfWaiting = sharableResource.getNumberOfWaitingOnCyclicBarrier();
            sharableResource.awaitCyclicBarrier();
            LOG.info("{} | Await on barrier: {}", threadName, numberOfWaiting);
        } catch (Throwable t) {
            handleRunningThrowable(t);
        }
    }
}
