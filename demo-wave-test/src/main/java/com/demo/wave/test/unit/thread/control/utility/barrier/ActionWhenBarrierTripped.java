package com.demo.wave.test.unit.thread.control.utility.barrier;

import com.demo.wave.test.unit.thread.utility.ContainerRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Vince Yuan
 * @date 2021/12/3
 */
public class ActionWhenBarrierTripped extends ContainerRunner {

    private static final Logger LOG = LoggerFactory.getLogger(ActionWhenBarrierTripped.class);

    @Override
    public void run() {
        LOG.info("Waiting is completed!");
    }
}
