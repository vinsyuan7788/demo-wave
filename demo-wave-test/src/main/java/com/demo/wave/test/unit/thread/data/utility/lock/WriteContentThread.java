package com.demo.wave.test.unit.thread.data.utility.lock;

import com.demo.wave.test.unit.thread.data.resource.SharableResource;
import com.demo.wave.test.unit.thread.utility.ContainerRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Vince Yuan
 * @date 2021/12/10
 */
public class WriteContentThread extends ContainerRunner {

    private static final Logger LOG = LoggerFactory.getLogger(WriteContentThread.class);

    private SharableResource sharableResource;

    public WriteContentThread(String threadName, SharableResource sharableResource) {
        setName(threadName);
        this.sharableResource = sharableResource;
    }

    @Override
    public void run() {
        try {
            String threadName = getName();
            for (int i = 0; i < 100; i++) {
                String currentContent = sharableResource.writeToSharableResource();
                LOG.info("{} | Current write content: {}", threadName, currentContent);
                sleep(100);
            }
        } catch (Throwable t) {
            handleRunningThrowable(t);
        }
    }
}
