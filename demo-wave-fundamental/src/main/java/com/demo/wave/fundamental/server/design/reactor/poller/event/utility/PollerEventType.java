package com.demo.wave.fundamental.server.design.reactor.poller.event.utility;

import com.demo.wave.fundamental.utility.exception.DemoException;

import java.nio.channels.SelectionKey;

/**
 * @author Vince Yuan
 * @date 2021/12/1
 */
public class PollerEventType {

    /**
     * This field is used to represent the registration of accepted socket channel
     */
    public static final int REGISTER_ACCEPTED_SOCKET_CHANNEL = 1;
    /**
     * This field is used to represent the completion of processing received packet
     */
    public static final int COMPLETE_PROCESSING_RECEIVED_PACKET = 2;
    /**
     * This field is used to represent the completion of responding new packet
     */
    public static final int COMPLETE_RESPONDING_NEW_PACKET = 3;

    /**
     * This method is used to get {@link SelectionKey}'s operation code
     * (i.e., {@link SelectionKey#OP_READ}, {@link SelectionKey#OP_WRITE},
     * {@link SelectionKey#OP_CONNECT}, {@link SelectionKey#OP_ACCEPT})
     *
     * @param pollerEventType
     * @return
     */
    public static int getSelectionKeyOpCode(int pollerEventType) {
        int selectionKeyOpCode;
        switch (pollerEventType) {
            case REGISTER_ACCEPTED_SOCKET_CHANNEL:
            case COMPLETE_RESPONDING_NEW_PACKET:
                selectionKeyOpCode = SelectionKey.OP_READ;
                break;
            case COMPLETE_PROCESSING_RECEIVED_PACKET:
                selectionKeyOpCode = SelectionKey.OP_WRITE;
                break;
            default:
                throw new DemoException("Poller event is unknown");
        }
        return selectionKeyOpCode;
    }
}
