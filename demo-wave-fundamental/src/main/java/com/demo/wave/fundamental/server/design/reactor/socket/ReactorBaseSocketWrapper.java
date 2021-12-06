package com.demo.wave.fundamental.server.design.reactor.socket;

import com.demo.wave.fundamental.server.design.reactor.handler.BaseHandler;
import com.demo.wave.fundamental.server.design.reactor.handler.ReactorBaseHandler;
import com.demo.wave.fundamental.server.design.reactor.handler.utility.HandlingType;
import com.demo.wave.fundamental.server.design.reactor.poller.BasePoller;
import com.demo.wave.fundamental.server.design.reactor.poller.utility.NeedProcessEvent;
import com.demo.wave.fundamental.server.design.reactor.processor.utility.ProcessingType;
import com.demo.wave.fundamental.server.design.reactor.selector.utility.NioSelectionKeyInfo;
import com.demo.wave.fundamental.utility.exception.DemoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Vince Yuan
 * @date 2021/12/2
 */
public class ReactorBaseSocketWrapper implements BaseSocketWrapper {

    private static final Logger LOG = LoggerFactory.getLogger(ReactorBaseSocketWrapper.class);

    private int processingType;
    private NioSelectionKeyInfo selectionKeyInfo;
    private BasePoller poller;
    private NeedProcessEvent needProcessEvent;

    public ReactorBaseSocketWrapper(int processingType, NioSelectionKeyInfo selectionKeyInfo, BasePoller poller, NeedProcessEvent needProcessEvent) {
        this.processingType = processingType;
        this.selectionKeyInfo = selectionKeyInfo;
        this.poller = poller;
        this.needProcessEvent = needProcessEvent;
    }

    @Override
    public void process() {
        try {
           getHandler().process();
        } catch (Throwable t) {
            handleProcessingThrowable(t);
        }
    }

    @Override
    public void handleProcessingThrowable(Throwable t) {
        LOG.info("Perform processing error", t);
    }

    @Override
    public NioSelectionKeyInfo getSelectionKeyInfo() {
        return selectionKeyInfo;
    }

    @Override
    public BasePoller getPoller() {
        return poller;
    }

    @Override
    public NeedProcessEvent getNeedProcessEvent() {
        return needProcessEvent;
    }

    /********************************************* Utility Method *********************************************/

    /**
     * This method is used to get a handler
     *
     * @return
     */
    private BaseHandler getHandler() {
        BaseHandler handler;
        switch (processingType) {
            case ProcessingType.MIDDLE_WARE:
                handler = new ReactorBaseHandler(HandlingType.MIDDLE_WARE, this);
                break;
            case ProcessingType.WEB_APPLICATION:
                handler = new ReactorBaseHandler(HandlingType.WEB_APPLICATION, this);
                break;
            default:
                throw new DemoException("Processing type is unknown");
        }
        return handler;
    }
}
