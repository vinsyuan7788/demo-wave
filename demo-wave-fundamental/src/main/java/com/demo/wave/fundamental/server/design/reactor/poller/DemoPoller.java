package com.demo.wave.fundamental.server.design.reactor.poller;

import com.demo.wave.fundamental.server.design.reactor.poller.utility.NeedProcessEvent;

import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author Vince Yuan
 * @date 2021/11/30
 */
public interface DemoPoller {

    /**
     * This method is used to start the poller
     */
    void startPoller();

    /**
     * This method is used to close the poller
     */
    void closePoller();

    /**
     * This method is used to handle throwable during initialization
     *
     * @param t
     */
    void handleInitializingThrowable(Throwable t);

    /**
     * This method is used to handle throwable during starting
     *
     * @param t
     */
    void handleStartingThrowable(Throwable t);

    /**
     * This method is used to handle throwable during closing
     *
     * @param t
     */
    void handleClosingThrowable(Throwable t);

    /**
     * This method is used to handle throwable during running
     *
     * @param t
     */
    void handleRunningThrowable(Throwable t);

    /**
     * This method is used to register the accepted socket channel
     *
     * @param acceptedSocketChannel
     */
    void register(SocketChannel acceptedSocketChannel);

    /**
     * This method is used to get the attachment queue
     *
     * @return
     */
    ConcurrentLinkedQueue<Object> getAttachmentQueue();

    /**
     * This method is used to select keys whose channels are ready for registered events.
     *
     * @param selector
     * @return
     * @throws Exception
     */
    int selectKeys(Selector selector) throws Exception;

    /**
     * This method is used to get selected keys whose channels are ready for
     * registered events.
     *
     * @param selector
     * @return
     * @throws Exception
     */
    Set<SelectionKey> getSelectedKeys(Selector selector);

    /**
     * This method is used to process selected keys whose channels are ready for
     * registered events.
     *
     * @param selectedKeys
     * @throws Exception
     */
    void processSelectedKeys(Set<SelectionKey> selectedKeys) throws Exception;

    /**
     * This method is used to handle throwable during registering
     *
     * @param t
     */
    void handleRegisteringThrowable(Throwable t);

    /**
     * This method is used to process accept event
     *
     * @param selectedKey
     * @throws Exception
     */
    void processAcceptEvent(SelectionKey selectedKey) throws Exception;

    /**
     * This method is used to process connect event
     *
     * @param selectedKey
     * @throws Exception
     */
    void processConnectEvent(SelectionKey selectedKey) throws Exception;

    /**
     * This method is used to process read event
     *
     * @param selectedKey
     * @throws Exception
     */
    void processReadEvent(SelectionKey selectedKey) throws Exception;

    /**
     * This method is used to process write event
     *
     * @param selectedKey
     * @throws Exception
     */
    void processWriteEvent(SelectionKey selectedKey) throws Exception;

    /**
     *
     * @param selectedKey
     * @return
     * @throws Exception
     */
    NeedProcessEvent needProcessReadEvent(SelectionKey selectedKey) throws Exception;

    /**
     *
     * @param attachmentQueue
     * @return
     * @throws Exception
     */
    NeedProcessEvent needProcessWriteEvent(Queue<Object> attachmentQueue) throws Exception;
}
