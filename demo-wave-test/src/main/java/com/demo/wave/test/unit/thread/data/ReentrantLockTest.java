package com.demo.wave.test.unit.thread.data;

import com.demo.wave.test.unit.thread.data.resource.SharableResource;
import com.demo.wave.test.unit.thread.data.utility.lock.AccessCountThread;
import com.demo.wave.test.unit.thread.data.utility.lock.AccessWriteContentThread;
import com.demo.wave.test.unit.thread.data.utility.lock.CountUpThread;
import com.demo.wave.test.unit.thread.data.utility.lock.ReadContentThread;
import com.demo.wave.test.unit.thread.data.utility.lock.WriteContentThread;
import com.demo.wave.test.unit.thread.utility.AccessCountResult;
import com.demo.wave.test.unit.thread.utility.AccessWriteContentResult;
import com.demo.wave.test.unit.thread.utility.ContainerRunner;
import com.demo.wave.test.unit.thread.utility.TestHelper;
import org.junit.After;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.Future;

/**
 * @author Vince Yuan
 * @date 2021/12/4
 */
public class ReentrantLockTest extends TestHelper {

    private static final Logger LOG = LoggerFactory.getLogger(ReentrantLockTest.class);

    private SharableResource sharableResource = new SharableResource();
    private ContainerRunner[] countUpThreads = new ContainerRunner[10];
    private ContainerRunner[] readContentThreads = new ContainerRunner[10];
    private ContainerRunner[] writeContentThreads = new ContainerRunner[10];
    private Callable<AccessCountResult>[] accessCountThreads = new AccessCountThread[10];
    private Callable<AccessWriteContentResult>[] accessWriteContentThreads = new AccessWriteContentThread[10];

    @Test
    public void performSingleTest1() throws Exception {
        for (int i = 0; i < countUpThreads.length; i++) {
            countUpThreads[i] = new CountUpThread("CountUpThread" + (i + 1), sharableResource);
            countUpThreads[i].start();
        }
        for (int i = 0; i < accessCountThreads.length; i++) {
            accessCountThreads[i] = new AccessCountThread("AccessCountThread" + (i + 1), sharableResource);
        }
        LOG.info("Waiting until the count is accessed (with first-completion-first-out) ...");
        ExecutorCompletionService<AccessCountResult> executorCompletionService = new ExecutorCompletionService<>(getThreadPool());
        // Access count per 200 millis
        for (int i = 0; i < accessCountThreads.length; i++) {
            executorCompletionService.submit(accessCountThreads[i]);
            Thread.sleep(200);
        }
        // If access count thread is waiting for the lock, then poll result is null here
        Future<AccessCountResult> future = executorCompletionService.poll();
        // If all access count threads wait for the lock until here, then directly ends the program
        while (future != null) {
            AccessCountResult result = future.get();
            LOG.info("{} | Access count: {}", result.getThreadName(), result.getCount());
            future = executorCompletionService.poll();
        }
        LOG.info("Waiting is completed...");
    }

    @Test
    public void performSingleTest2() throws Exception {
        for (int i = 0; i < countUpThreads.length; i++) {
            countUpThreads[i] = new CountUpThread("CountUpThread" + (i + 1), sharableResource);
            countUpThreads[i].start();
        }
        for (int i = 0; i < accessCountThreads.length; i++) {
            accessCountThreads[i] = new AccessCountThread("AccessCountThread" + (i + 1), sharableResource);
        }
        LOG.info("Waiting until the count is accessed (with first-submit-first-out) ...");
        List<Future<AccessCountResult>> futureList = new ArrayList<>();
        ExecutorCompletionService<AccessCountResult> executorCompletionService = new ExecutorCompletionService<>(getThreadPool());
        // Access count per 200 millis
        for (int i = 0; i < accessCountThreads.length; i++) {
            Future<AccessCountResult> future = executorCompletionService.submit(accessCountThreads[i]);
            futureList.add(future);
            Thread.sleep(200);
        }
        for (Future<AccessCountResult> future : futureList) {
            // If access count thread is waiting for the lock, then this method will be blocked
            AccessCountResult result = future.get();
            LOG.info("{} | Access count: {}", result.getThreadName(), result.getCount());
        }
        LOG.info("Waiting is completed...");
    }

    @Test
    public void performSingleTest3() throws Exception {
        for (int i = 0; i < readContentThreads.length; i++) {
            readContentThreads[i] = new ReadContentThread("ReadContentThread" + (i + 1), sharableResource);
            readContentThreads[i].start();
        }
        LOG.info("Waiting until the read content is accessed (with first-completion-first-out) ...");
        for (int i = 0; i < readContentThreads.length; i++) {
            readContentThreads[i].join();
        }
        LOG.info("Waiting is completed...");
    }

    @Test
    public void performSingleTest4() throws Exception {
        for (int i = 0; i < writeContentThreads.length; i++) {
            writeContentThreads[i] = new WriteContentThread("WriteContentThread" + (i + 1), sharableResource);
            writeContentThreads[i].start();
        }
        for (int i = 0; i < accessWriteContentThreads.length; i++) {
            accessWriteContentThreads[i] = new AccessWriteContentThread("AccessWriteContentThread" + (i + 1), sharableResource);
        }
        LOG.info("Waiting until the write content is accessed (with first-completion-first-out) ...");
        List<Future<AccessWriteContentResult>> futureList = new ArrayList<>();
        ExecutorCompletionService<AccessWriteContentResult> executorCompletionService = new ExecutorCompletionService<>(getThreadPool());
        // Access count per 200 millis
        for (int i = 0; i < accessWriteContentThreads.length; i++) {
            Future<AccessWriteContentResult> future = executorCompletionService.submit(accessWriteContentThreads[i]);
            futureList.add(future);
            Thread.sleep(200);
        }
        for (Future<AccessWriteContentResult> future : futureList) {
            // If access count thread is waiting for the lock, then this method will be blocked
            AccessWriteContentResult result = future.get();
            LOG.info("{} | Access write content: {}", result.getThreadName(), result.getWriteContent());
        }
        LOG.info("Waiting is completed...");
    }

    @After
    public void completeTest() {
        sharableResource.cleanCount();
        sharableResource.cleanReadContent();
        sharableResource.cleanWriteContent();
    }
}
