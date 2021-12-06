package com.demo.wave.test.unit.thread.control;

import com.demo.wave.test.unit.thread.control.resource.SharableResource;
import com.demo.wave.test.unit.thread.control.utility.exchanger.ExchangerThread1;
import com.demo.wave.test.unit.thread.control.utility.exchanger.ExchangerThread2;
import com.demo.wave.test.unit.thread.utility.ContainerRunner;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Vince Yuan
 * @date 2021/12/4
 */
public class ExchangerTest {

    private static final Logger LOG = LoggerFactory.getLogger(ExchangerTest.class);

    private SharableResource sharableResource = new SharableResource();
    private ContainerRunner[] threads = new ContainerRunner[2];

    @Before
    public void prepareTest() throws Exception {
        threads[0] = new ExchangerThread1("ExchangeThread1", sharableResource);
        threads[1] = new ExchangerThread2("ExchangeThread2", sharableResource);
    }

    @Test
    public void performTest() throws Exception {
        LOG.info("Waiting until the exchanger completes data exchange...");
        threads[0].start();
        threads[1].start();
        threads[0].join();
        threads[1].join();
        LOG.info("Waiting is completed...");
    }
}
