package com.demo.wave.fundamental.server.business.web.processor;

import com.demo.wave.fundamental.server.business.common.processor.BaseProcessor;
import com.demo.wave.fundamental.server.design.reactor.socket.BaseSocketWrapper;
import com.demo.wave.fundamental.server.business.middleware.utility.data.network.packet.Packet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Vince Yuan
 * @date 2021/12/1
 */
public class WebApplicationBaseProcessor implements BaseProcessor {

    private static final Logger LOG = LoggerFactory.getLogger(WebApplicationBaseProcessor.class);

    private BaseSocketWrapper socketWrapper;

    public WebApplicationBaseProcessor(BaseSocketWrapper socketWrapper) {
        try {
            this.socketWrapper = socketWrapper;
        } catch (Throwable t) {
            handleInitializingThrowable(t);
        }
    }

    @Override
    public void process() {
        try {
            processReadEvent();
        } catch (Throwable t) {
            handleProcessingThrowable(t);
        }
    }

    @Override
    public void handleProcessingThrowable(Throwable t) {
        LOG.error("Perform processing error", t);
    }

    @Override
    public void processReadEvent() throws Exception {

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
