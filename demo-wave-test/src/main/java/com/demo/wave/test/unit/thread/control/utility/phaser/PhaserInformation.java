package com.demo.wave.test.unit.thread.control.utility.phaser;

import java.util.concurrent.Phaser;

/**
 * @author Vince Yuan
 * @date 2021/12/4
 */
public class PhaserInformation {

    private int registeredParties;
    private int arrivedParties;
    private int unarrivedParties;
    private int phase;
    private Phaser root;
    private Phaser parent;

    public PhaserInformation(int registeredParties, int arrivedParties, int unarrivedParties, int phase, Phaser root, Phaser parent) {
        this.registeredParties = registeredParties;
        this.arrivedParties = arrivedParties;
        this.unarrivedParties = unarrivedParties;
        this.phase = phase;
        this.root = root;
        this.parent = parent;
    }

    public static PhaserInformation create(int registeredParties, int arrivedParties, int unarrivedParties, int phase, Phaser root, Phaser parent) {
        return new PhaserInformation(registeredParties, arrivedParties, unarrivedParties, phase, root, parent);
    }

    @Override
    public String toString() {
            return "PhaserInformation{" +
                    "registeredParties=" + registeredParties +
                    ", arrivedParties=" + arrivedParties +
                    ", unarrivedParties=" + unarrivedParties +
                    ", phase=" + phase +
                    ", root=" + root +
                    ", parent=" + parent +
                    '}';
    }
}
