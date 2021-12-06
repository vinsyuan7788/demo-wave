package com.demo.wave.test.unit.thread.control.utility.exchanger;

import com.demo.wave.test.unit.thread.control.resource.SharableResource;
import com.demo.wave.test.unit.thread.utility.ContainerRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;

/**
 * @author Vince Yuan
 * @date 2021/12/4
 */
public class ExchangerThread1 extends ContainerRunner {

    private static final Logger LOG = LoggerFactory.getLogger(ExchangerThread1.class);

    private SharableResource sharableResource;
    private InformationToExchange data;

    public ExchangerThread1(String threadName, SharableResource sharableResource) {
        setName(threadName);
        this.sharableResource = sharableResource;
        this.data = new InformationToExchange(1L, "Vince", BigDecimal.valueOf(30000L));
    }

    @Override
    public void run() {
        try {
            String threadName = getName();
            InformationToExchange exchangedData = (InformationToExchange) sharableResource.exchange(data);
            LOG.info("{} | Data exchanged: {}", threadName, exchangedData);
        } catch (Throwable t) {
            handleRunningThrowable(t);
        }
    }
}
