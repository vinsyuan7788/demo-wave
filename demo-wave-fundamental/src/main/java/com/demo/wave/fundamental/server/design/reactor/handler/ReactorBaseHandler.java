package com.demo.wave.fundamental.server.design.reactor.handler;

import com.demo.wave.fundamental.server.business.common.processor.BaseProcessor;
import com.demo.wave.fundamental.server.business.middleware.processor.MiddlewareBaseProcessor;
import com.demo.wave.fundamental.server.business.web.processor.WebApplicationBaseProcessor;
import com.demo.wave.fundamental.server.design.reactor.socket.BaseSocketWrapper;
import com.demo.wave.fundamental.utility.exception.DemoException;
import com.demo.wave.fundamental.server.design.reactor.handler.utility.HandlingType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Vince Yuan
 * @date 2021/12/1
 */
public class ReactorBaseHandler implements BaseHandler {

    private static final Logger LOG = LoggerFactory.getLogger(ReactorBaseHandler.class);

    private BaseProcessor processor;

    public ReactorBaseHandler(int handlingType, BaseSocketWrapper socketWrapper) {
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
    private BaseProcessor createProcessor(int handlingType, BaseSocketWrapper socketWrapper) {
        BaseProcessor processor;
        switch (handlingType) {
            case HandlingType.MIDDLE_WARE:
                processor = new MiddlewareBaseProcessor(socketWrapper);
                break;
            case HandlingType.WEB_APPLICATION:
                processor = new WebApplicationBaseProcessor(socketWrapper);
                break;
            default:
                throw new DemoException("Handling type is unknown");
        }
        return processor;
    }
}
