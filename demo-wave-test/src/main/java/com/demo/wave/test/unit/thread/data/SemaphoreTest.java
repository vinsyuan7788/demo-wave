package com.demo.wave.test.unit.thread.data;

import com.demo.wave.test.unit.thread.data.resource.SharableResource;
import com.demo.wave.test.unit.thread.data.utility.semaphore.AccessCountThread;
import com.demo.wave.test.unit.thread.data.utility.semaphore.CountUpThread;
import com.demo.wave.test.unit.thread.utility.AccessCountResult;
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
 * @date 2021/12/3
 */
public class SemaphoreTest extends TestHelper {

    private static final Logger LOG = LoggerFactory.getLogger(SemaphoreTest.class);

    private SharableResource sharableResource = new SharableResource();
    private ContainerRunner[] countUpThreads = new ContainerRunner[10];
    private Callable<AccessCountResult>[] accessCountThreads = new AccessCountThread[10];

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

    @After
    public void completeTest() {
        sharableResource.cleanCount();
    }
}
