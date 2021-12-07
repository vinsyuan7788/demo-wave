package com.demo.wave.test.unit.atomic;

import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Vince Yuan
 * @date 2021/11/30
 */
public class AtomicIntegerTest {

    @Test
    public void testAtomicInteger() {
        AtomicInteger ai = new AtomicInteger(Integer.MAX_VALUE);
        System.out.println(ai);
        ai.incrementAndGet();
        System.out.println(ai);
    }

    @Test
    public void testRoundBin() {
        AtomicInteger ai = new AtomicInteger(Integer.MAX_VALUE);
        System.out.println(Math.abs(ai.get() % 3));
        ai.incrementAndGet();
        System.out.println(Math.abs(ai.get() % 3));
    }
}
