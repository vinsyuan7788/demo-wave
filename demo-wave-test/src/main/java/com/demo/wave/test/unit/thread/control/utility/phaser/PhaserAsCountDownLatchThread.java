package com.demo.wave.test.unit.thread.control.utility.phaser;

import com.demo.wave.test.unit.thread.control.resource.SharableResource;
import com.demo.wave.test.unit.thread.utility.ContainerRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Vince Yuan
 * @date 2021/12/4
 */
public class PhaserAsCountDownLatchThread extends ContainerRunner {

    private static final Logger LOG = LoggerFactory.getLogger(PhaserAsCountDownLatchThread.class);

    private SharableResource sharableResource;

    public PhaserAsCountDownLatchThread(String threadName, SharableResource sharableResource) {
        setName(threadName);
        this.sharableResource = sharableResource;
    }

    @Override
    public void run() {
        try {
            String threadName = getName();
            PhaserInformation phaserInformation = sharableResource.getPhaserInformation(sharableResource.getPhaserAsCountDownLatch());
            int currentPhase = sharableResource.arriveAndDeregister(sharableResource.getPhaserAsCountDownLatch());
            LOG.info("{} | Await on phase: {} | phaser information: {}", threadName, currentPhase, phaserInformation);
        } catch (Throwable t) {
            handleRunningThrowable(t);
        }
    }
}
