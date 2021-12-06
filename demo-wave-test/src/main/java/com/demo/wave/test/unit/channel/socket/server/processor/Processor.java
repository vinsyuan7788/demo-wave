package com.demo.wave.test.unit.channel.socket.server.processor;

import com.demo.wave.fundamental.utility.exception.DemoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Vince Yuan
 * @date 2021/12/6
 */
public abstract class Processor extends Thread {

    private static final Logger LOG = LoggerFactory.getLogger(Processor.class);

    private final String className = getClass().getName();

    public void logModeAndEventIfDifferent(String mode, int readyOps) {
        String event = getSelectionKeyEvent(readyOps);
        if (event.equalsIgnoreCase(mode) || event.contains(mode)) {
            return;
        } else {
            LOG.info("{}#logModeAndEventIfDifferent | mode: {} | ready event: {}", className, mode, event);
        }
    }

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
}
