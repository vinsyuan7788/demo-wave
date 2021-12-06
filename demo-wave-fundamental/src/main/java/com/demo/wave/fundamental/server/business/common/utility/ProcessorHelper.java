package com.demo.wave.fundamental.server.business.common.utility;

import com.demo.wave.common.utility.LogUtils;
import com.demo.wave.fundamental.utility.exception.DemoException;
import com.demo.wave.fundamental.utility.information.SelectionKeyInfoProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.channels.SelectionKey;

/**
 * @author Vince Yuan
 * @date 2021/12/2
 */
public abstract class ProcessorHelper implements SelectionKeyInfoProvider {

    private static final Logger LOG = LoggerFactory.getLogger(ProcessorHelper.class);

    private String className = getClass().getSimpleName();

    @Override
    public String getSelectionKeyEvent(int opCode) {
        String event;
        switch (opCode) {
            // Single event
            case 1:
                event = "read";
                break;
            case 4:
                event = "write";
                break;
            case 8:
                event = "connect";
                break;
            case 16:
                event = "accept";
                break;
            // Double event:
            case 5:
                event = "read, write";
                break;
            case 9:
                event = "read, connect";
                break;
            case 17:
                event = "read, accept";
                break;
            case 12:
                event = "write, connect";
                break;
            case 20:
                event = "write, accept";
                break;
            case 24:
                event = "connect, accept";
                break;
            // Triple event
            case 13:
                event = "read, write, connect";
                break;
            case 21:
                event = "read, write, accept";
                break;
            case 25:
                event = "read, connect, accept";
                break;
            case 28:
                event = "write, connect, accept";
                break;
            // Quadruple event
            case 29:
                event = "read, write, connect, accept";
                break;
            default:
                throw new DemoException("Selection key event code unknown");
        }
        return event;
    }

    @Override
    public void logSelectionKeyInfo(SelectionKey selectedKey, int selectedEventCode, SelectionKey registeredKey, int registeredEventCode) {
        LOG.info(LogUtils.getMessage(className + "#logSelectionKeyInfo", "selected key: {} | selected events: {} | registered key: {} | registered events: {}"),
                selectedKey, getSelectionKeyEvent(selectedEventCode), registeredKey, getSelectionKeyEvent(registeredEventCode));
    }
}
