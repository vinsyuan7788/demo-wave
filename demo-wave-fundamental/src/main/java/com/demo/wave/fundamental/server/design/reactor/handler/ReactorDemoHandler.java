package com.demo.wave.fundamental.server.design.reactor.handler;

import com.demo.wave.fundamental.server.business.common.processor.DemoProcessor;
import com.demo.wave.fundamental.server.business.middleware.processor.MiddlewareDemoProcessor;
import com.demo.wave.fundamental.server.business.web.processor.WebApplicationDemoProcessor;
import com.demo.wave.fundamental.server.design.reactor.socket.DemoSocketWrapper;
import com.demo.wave.fundamental.utility.exception.DemoException;
import com.demo.wave.fundamental.server.design.reactor.handler.utility.HandlingType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Vince Yuan
 * @date 2021/12/1
 */
public class ReactorDemoHandler implements DemoHandler {

    private static final Logger LOG = LoggerFactory.getLogger(ReactorDemoHandler.class);

    private DemoProcessor processor;

    public ReactorDemoHandler(int handlingType, DemoSocketWrapper socketWrapper) {
        try{
            this.processor = createProcessor(handlingType, socketWrapper);
        } catch (Throwable t) {
            handleInitializingThrowable(t);
        }
    }

    @Override
    public void process() {
        try {
            processor.process();
        } catch (Throwable t) {
            handleProcessingThrowable(t);
        }
    }

    @Override
    public void handleProcessingThrowable(Throwable t) {
        LOG.error("Perform processing error", t);
    }

    @Override
    public void handleInitializingThrowable(Throwable t) {
        LOG.error("Create handler error", t);
    }

    /*********************************** Utility Method ***********************************/

    /**
     * This method is used to create a processor
     *
     * @param handlingType
     * @param socketWrapper
     * @return
     */
    private DemoProcessor createProcessor(int handlingType, DemoSocketWrapper socketWrapper) {
        DemoProcessor processor;
        switch (handlingType) {
            case HandlingType.MIDDLE_WARE:
                processor = new MiddlewareDemoProcessor(socketWrapper);
                break;
            case HandlingType.WEB_APPLICATION:
                processor = new WebApplicationDemoProcessor(socketWrapper);
                break;
            default:
                throw new DemoException("Handling type is unknown");
        }
        return processor;
    }
}
