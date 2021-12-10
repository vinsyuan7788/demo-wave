package com.demo.wave.test.unit.thread.data.resource;

import com.demo.wave.common.utility.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author Vince Yuan
 * @date 2021/12/4
 */
public class SharableResource {

    private static final Logger LOG = LoggerFactory.getLogger(SharableResource.class);

    private ReentrantLock lock = new ReentrantLock();;
    private ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
    private ReentrantReadWriteLock.ReadLock readLock = readWriteLock.readLock();
    private ReentrantReadWriteLock.WriteLock writeLock = readWriteLock.writeLock();
    public Semaphore semaphore = new Semaphore(1);

    /********************************** Reentrant Lock **********************************/

    private Long count = 0L;
    private Condition accessible = lock.newCondition();

    /**
     * This method is used to count up through lock
     */
    public Long countUpWithLock() {
        Long currentCount = 0L;
        lock.lock();
        try {
            count += 1;
            currentCount = count;
        } catch (Throwable t) {
            LOG.error("Exception caught", t);
        } finally {
            lock.unlock();
        }
        return currentCount;
    }

    /**
     * This method is used to access the shared count
     *
     * @return
     */
    public Long accessCountWithLockAndCondition() {
        Long count = 0L;
        lock.lock();
        try {
            if (this.count < 100) {
                accessible.await();
            } else {
                count = this.count;
                this.count = this.count - 100;
                accessible.signalAll();
            }
        } catch (Throwable t) {
            LOG.error("Exception caught", t);
        } finally {
            lock.unlock();
        }
        return count;
    }

    public void cleanCount() {
        this.count = 0L;
    }

    /******************************* Read Lock & Write Lock *******************************/

    private Long readContentAccumulator = 0L;
    private Long writeContentAccumulator = 0L;
    private String readContent = StringUtils.BLANK_STRING;
    private String writeContent = StringUtils.BLANK_STRING;
    private Condition writable = writeLock.newCondition();

    /**
     * This method is used to read content to sharable resource
     *
     * @return
     */
    public String readToSharableResource() {
        String currentContent = StringUtils.BLANK_STRING;
        readLock.lock();
        try {
            this.readContent = this.readContent + (
                    StringUtils.BLANK_STRING.equalsIgnoreCase(readContent) ?
                    readContentAccumulator.toString() :
                    ", " + readContentAccumulator.toString()
            );
            readContentAccumulator += 1;
            currentContent = this.readContent;
        } catch (Throwable t) {
            LOG.error("Exception caught", t);
        } finally {
            readLock.unlock();
        }
        return currentContent;
    }

    /**
     * This method is used to write content to sharable resource
     *
     * @return
     */
    public String writeToSharableResource() {
        String currentContent = StringUtils.BLANK_STRING;
        writeLock.lock();
        try {
            this.writeContent = this.writeContent + (
                    StringUtils.BLANK_STRING.equalsIgnoreCase(writeContent) ?
                            writeContentAccumulator.toString() :
                            ", " + writeContentAccumulator.toString()
            );
            writeContentAccumulator += 1;
            currentContent = this.writeContent;
        } catch (Throwable t) {
            LOG.error("Exception caught", t);
        } finally {
            writeLock.unlock();
        }
        return currentContent;
    }

    /**
     * This method is used to access the shared write content
     *
     * @return
     */
    public String accessWriteContentWithLockAndCondition() {
        String writeContent = StringUtils.BLANK_STRING;
        writeLock.lock();
        try {
            if (this.writeContentAccumulator < 100) {
                writable.await();
            } else {
                writeContent = this.writeContent;
                this.writeContent = StringUtils.BLANK_STRING;
                this.writeContentAccumulator = this.writeContentAccumulator - 100;
                writable.signalAll();
            }
        } catch (Throwable t) {
            LOG.error("Exception caught", t);
        } finally {
            writeLock.unlock();
        }
        return writeContent;
    }

    public void cleanReadContent() {
        this.readContentAccumulator = 0L;
        this.readContent = StringUtils.BLANK_STRING;
    }

    public void cleanWriteContent() {
        this.writeContentAccumulator = 0L;
        this.writeContent = StringUtils.BLANK_STRING;
    }

    /******************************* Semaphore *******************************/

    /**
     * This method is used to count up with the control of semaphore
     */
    public Long countUpWithSemaphore() {
        Long currentCount = 0L;
        try {
            semaphore.acquire();
            count += 1;
            currentCount = count;
        } catch (Throwable t) {
            LOG.error("Exception caught", t);
        }
        return currentCount;
    }

    /**
     * This method is used to access the shared count with the control of semaphore
     *
     * @return
     */
    public Long accessCountWithSemaphore() {
        try {
            semaphore.release();
        } catch (Throwable t) {
            LOG.error("Exception caught", t);
        }
        return count;
    }
}
