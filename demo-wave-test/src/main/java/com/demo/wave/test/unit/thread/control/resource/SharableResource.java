package com.demo.wave.test.unit.thread.control.resource;

import com.demo.wave.test.unit.thread.control.utility.barrier.ActionWhenBarrierTripped;
import com.demo.wave.test.unit.thread.control.utility.phaser.CustomPhaserAsCountDownLatch;
import com.demo.wave.test.unit.thread.control.utility.phaser.CustomPhaserAsCyclicBarrier;
import com.demo.wave.test.unit.thread.control.utility.phaser.PhaserInformation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.misc.Unsafe;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.Exchanger;
import java.util.concurrent.Phaser;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Vince Yuan
 * @date 2021/12/3
 */
public class SharableResource {

    private static final Logger LOG = LoggerFactory.getLogger(SharableResource.class);

    private final int numberOfThread = 100;
    /**
     * A sharable object based on {@link AbstractQueuedSynchronizer} and {@link Unsafe}.
     */
    private final CountDownLatch countDownLatch;
    /**
     * A sharable object based on {@link ReentrantLock} and {@link Condition}.
     */
    private final CyclicBarrier cyclicBarrier;
    /**
     * A sharable object based on {@link Unsafe}
     */
    private final Phaser phaserAsCountDownLatch;
    /**
     * A sharable object based on {@link Unsafe}
     */
    private final Phaser phaserAsCyclicBarrier;
    /**
     * A sharable object based on {@link Unsafe}
     */
    private Exchanger<Object> exchanger;

    public SharableResource() {
        this.countDownLatch = new CountDownLatch(getNumberOfThread());
        this.cyclicBarrier = new CyclicBarrier(getNumberOfThread() + 1, new ActionWhenBarrierTripped());
        this.phaserAsCountDownLatch = new CustomPhaserAsCountDownLatch();
        this.phaserAsCountDownLatch.bulkRegister(getNumberOfThread() + 1);
        this.phaserAsCyclicBarrier = new CustomPhaserAsCyclicBarrier();
        this.phaserAsCyclicBarrier.bulkRegister(getNumberOfThread() + 1);
        this.exchanger = new Exchanger<>();
    }

    /************************************ Getter and Setter ************************************/

    /**
     * This method is used to get the number of thread
     *
     * @return
     */
    public int getNumberOfThread() {
        return numberOfThread;
    }

    public CountDownLatch getCountDownLatch() {
        return countDownLatch;
    }

    public CyclicBarrier getCyclicBarrier() {
        return cyclicBarrier;
    }

    public Phaser getPhaserAsCountDownLatch() {
        return phaserAsCountDownLatch;
    }

    public Phaser getPhaserAsCyclicBarrier() {
        return phaserAsCyclicBarrier;
    }

    public Exchanger getExchanger() {
        return exchanger;
    }

    /************************************ Count Down Latch ************************************/

    /**
     * This method is used to count down {@link CountDownLatch}.
     */
    public void countDownLatch() {
        getCountDownLatch().countDown();
    }

    /**
     * This method is used to get the count of {@link CountDownLatch}.
     *
     * @return
     */
    public long getCountOfLatch() {
        return getCountDownLatch().getCount();
    }

    /**
     * This method is used to count down {@link CountDownLatch}. <br/>
     * Notice that in real application, we just need to invoke {@link CountDownLatch#countDown()}
     * to count down the latch (i.e., no need to add a synchronized keyword, here adding this keyword
     * is just to ensure the count down result can be return within an atomic operation). <br/>
     * For some concurrent technique such as {@link CyclicBarrier}, it is not compatible with "synchronized"
     * keyword, hence for a better thumb of use, do not "synchronized" with other concurrent technique in
     * real application (see {@link #countDownLatch()}).
     *
     * @param prefix
     */
    public synchronized void countDownLatch(String prefix) {
        getCountDownLatch().countDown();
        logCurrentCountOfCountDownLatch(prefix);
    }

    /**
     * This method is used to await the latch until it is counted down to zero
     *
     * @throws Exception
     */
    public void awaitCountDownLatch() throws Exception {
        getCountDownLatch().await();
    }

    /**
     * This method is used to log the current count of latch
     * 
     * @param prefix
     */
    private void logCurrentCountOfCountDownLatch(String prefix) {
        LOG.info("{} | Count down latch | current count: {}", prefix, getCountDownLatch().getCount());
    }
    
    /************************************ Cyclic Barrier ************************************/

    /**
     * This method is used to cause current thread to wait on {@link CyclicBarrier} until the
     * number of threads that wait on this barrier reaches the given number (i.e., parties)
     *
     * @throws Exception
     */
    public void awaitCyclicBarrier() throws Exception {
        getCyclicBarrier().await();
    }

    /**
     * This method is used to get the number of threads waiting on {@link CyclicBarrier}
     *
     * @return
     * @throws Exception
     */
    public int getNumberOfWaitingOnCyclicBarrier() throws Exception {
        return getCyclicBarrier().getNumberWaiting();
    }

    /**
     * This method is used to cause current thread to wait on the barrier until the number
     * of threads that wait on this barrier reaches the given number (i.e., parties) <br/>
     * Notice the keyword "synchronized" CANNOT be added on this method, since if doing so,
     * the method of {@link CyclicBarrier} will NOT work as expected. The reason is that
     * {@link CyclicBarrier#await()} will suspend current thread and therefore the "synchronized"
     * lock will NOT be released as well, which will prevent other threads invoking this method
     * and eventually blocking the entire program (we can try it by adding "synchronized" on this
     * method and see what will happen). <br/>
     * In real application, we just directly use {@link #awaitCyclicBarrier()}.
     *
     * @throws Exception
     */
    public void awaitCyclicBarrier(String prefix) throws Exception {
        logCurrentNumberWaitingOfCyclicBarrier(prefix);
        getCyclicBarrier().await();
    }

    /**
     * This method is used to log current number waiting of barrier. <br/>
     *
     * @param prefix
     */
    private void logCurrentNumberWaitingOfCyclicBarrier(String prefix) {
        LOG.info("{} | Await on barrier | the number of waiting: {}", prefix, getCyclicBarrier().getNumberWaiting());
    }

    /************************************ Phaser ************************************/

    /**
     * This method is used to arrive the phase of {@link Phaser} and de-registers from it
     * without waiting the arrival of other threads.
     *
     * @param phaser
     * @return
     */
    public int arriveAndDeregister(Phaser phaser) {
        return phaser.arriveAndDeregister();
    }

    /**
     * This method is used to arrive the phase of {@link Phaser} and wait until other
     * threads also arrive the phase.
     *
     * @return
     */
    public int arrivePhaserAndAwaitAdvance(Phaser phaser) {
        return phaser.arriveAndAwaitAdvance();
    }

    /**
     * This method is used to get the information of {@link Phaser}
     *
     * @param phaser
     * @return
     */
    public PhaserInformation getPhaserInformation(Phaser phaser) {
        return PhaserInformation.create(phaser.getRegisteredParties(), phaser.getArrivedParties(), phaser.getUnarrivedParties(), phaser.getPhase(), phaser.getRoot(), phaser.getParent());
    }

    /**
     * This method is used to arrive the phase of {@link Phaser} and wait until other
     * threads also arrive the phase. <br/>
     * Using this method means using phaser as {@link CyclicBarrier}.
     * In real application, we just directly use {@link #arrivePhaserAndAwaitAdvance}
     *
     * @param prefix
     */
    public void arrivePhaserAndAwaitAdvance(String prefix) {
        LOG.info("{} | Arrive on phaser | current phase: {} | registered parties: {} | arrived parties: {} | unarrived parties: {}", prefix, getPhaserAsCyclicBarrier().arriveAndAwaitAdvance(),
                getPhaserAsCyclicBarrier().getRegisteredParties(), getPhaserAsCyclicBarrier().getArrivedParties(), getPhaserAsCyclicBarrier().getUnarrivedParties());
    }

    /************************************ Exchanger ************************************/

    /**
     * This method is used to (wait for another thread at exchange point if another
     * thread has not arrived yet, and) provide data to another thread and return
     * the data received from another thread.
     *
     * @param data
     * @return
     * @throws Exception
     */
    public Object exchange(Object data) throws Exception {
        return getExchanger().exchange(data);
    }
}
