package com.demo.wave.fundamental.server.business.web.processor;

import com.demo.wave.fundamental.server.business.common.processor.DemoProcessor;
import com.demo.wave.fundamental.server.business.middleware.utility.data.network.packet.Packet;
import com.demo.wave.fundamental.server.design.reactor.socket.DemoSocketWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Vince Yuan
 * @date 2021/12/1
 */
public class WebApplicationDemoProcessor implements DemoProcessor {

    private static final Logger LOG = LoggerFactory.getLogger(WebApplicationDemoProcessor.class);

    private DemoSocketWrapper socketWrapper;

    public WebApplicationDemoProcessor(DemoSocketWrapper socketWrapper) {
        try {
            this.socketWrapper = socketWrapper;
        } catch (Throwable t) {
            handleInitializingThrowable(t);
        }
    }

    @Override
    public void process() {
        try {
            if (isReadEvent()) {
                processReadEvent();
            }
            if (isWriteEvent()) {
                processWriteEvent();
            }
        } catch (Throwable t) {
            handleProcessingThrowable(t);
        }
    }

    @Override
    public void handleProcessingThrowable(Throwable t) {
        LOG.error("Perform processing error", t);
    }

    @Override
    public boolean isReadEvent() {
        return false;
    }

    @Override
    public boolean isWriteEvent() {
        return false;
    }

    @Override
    public void processReadEvent() throws Exception {

    }

    @Override
    public void processWriteEvent() throws Exception {

    }

    @Override
    public Packet processReceivedPacket(Packet receivedPacket) {
        return null;
    }

    @Override
    public void handleInitializingThrowable(Throwable t) {
        LOG.error("Create processor error", t);
    }
}
