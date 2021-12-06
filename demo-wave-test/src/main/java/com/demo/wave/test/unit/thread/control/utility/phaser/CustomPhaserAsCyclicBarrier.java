package com.demo.wave.test.unit.thread.control.utility.phaser;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Phaser;

/**
 * @author Vince Yuan
 * @date 2021/12/3
 */
public class CustomPhaserAsCyclicBarrier extends Phaser {

    private static final Logger LOG = LoggerFactory.getLogger(CustomPhaserAsCyclicBarrier.class);

    private final String className = getClass().getSimpleName();

    public CustomPhaserAsCyclicBarrier() {
        super();
    }

    @Override
    protected boolean onAdvance(int phase, int registeredParties) {
        LOG.info("{}#onAdvance | phase: {} | registeredParties: {}", className, phase, registeredParties);
        return super.onAdvance(phase, registeredParties);
    }
}
