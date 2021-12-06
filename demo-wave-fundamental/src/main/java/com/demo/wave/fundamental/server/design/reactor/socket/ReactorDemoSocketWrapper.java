package com.demo.wave.fundamental.server.design.reactor.socket;

import com.demo.wave.fundamental.server.design.reactor.handler.DemoHandler;
import com.demo.wave.fundamental.server.design.reactor.handler.ReactorDemoHandler;
import com.demo.wave.fundamental.server.design.reactor.handler.utility.HandlingType;
import com.demo.wave.fundamental.server.design.reactor.poller.DemoPoller;
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
public class ReactorDemoSocketWrapper implements DemoSocketWrapper {

    private static final Logger LOG = LoggerFactory.getLogger(ReactorDemoSocketWrapper.class);

    private int processingType;
    private NioSelectionKeyInfo selectionKeyInfo;
    private DemoPoller poller;
    private NeedProcessEvent needProcessEvent;
    private int socketEventType;

    public ReactorDemoSocketWrapper(int processingType, NioSelectionKeyInfo selectionKeyInfo, DemoPoller poller, NeedProcessEvent needProcessEvent, int socketEventType) {
        this.processingType = processingType;
        this.selectionKeyInfo = selectionKeyInfo;
        this.poller = poller;
        this.needProcessEvent = needProcessEvent;
        this.socketEventType = socketEventType;
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
    public DemoPoller getPoller() {
        return poller;
    }

    @Override
    public NeedProcessEvent getNeedProcessEvent() {
        return needProcessEvent;
    }

    @Override
    public int getSocketEventType() {
        return socketEventType;
    }

    /********************************************* Utility Method *********************************************/

    /**
     * This method is used to get a handler
     *
     * @return
     */
    private DemoHandler getHandler() {
        DemoHandler handler;
        switch (processingType) {
            case ProcessingType.MIDDLE_WARE:
                handler = new ReactorDemoHandler(HandlingType.MIDDLE_WARE, this);
                break;
            case ProcessingType.WEB_APPLICATION:
                handler = new ReactorDemoHandler(HandlingType.WEB_APPLICATION, this);
                break;
            default:
                throw new DemoException("Processing type is unknown");
        }
        return handler;
    }
}
