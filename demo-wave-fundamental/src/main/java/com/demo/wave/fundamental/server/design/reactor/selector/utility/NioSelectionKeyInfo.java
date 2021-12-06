package com.demo.wave.fundamental.server.design.reactor.selector.utility;

import java.nio.channels.SelectableChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;

/**
 * @author Vince Yuan
 * @date 2021/12/2
 */
public class NioSelectionKeyInfo {

    private SelectionKey selectionKey;
    private SelectableChannel selectableChannel;
    private Selector selector;
    private Object attachment;
    private int interestOps;
    private int readyOps;

    private NioSelectionKeyInfo(SelectionKey selectionKey, SelectableChannel selectableChannel, Selector selector, Object attachment, int interestOps, int readyOps) {
        this.selectionKey = selectionKey;
        this.selectableChannel = selectableChannel;
        this.selector = selector;
        this.attachment = attachment;
        this.interestOps = interestOps;
        this.readyOps = readyOps;
    }

    public static NioSelectionKeyInfo create(SelectionKey selectionKey, SelectableChannel selectableChannel, Selector selector, Object attachment, int interestOps, int readyOps) {
        return new NioSelectionKeyInfo(selectionKey, selectableChannel, selector, attachment, interestOps, readyOps);
    }

    public SelectionKey getSelectionKey() {
        return selectionKey;
    }

    public SelectableChannel getSelectableChannel() {
        return selectableChannel;
    }

    public Selector getSelector() {
        return selector;
    }

    public Object getAttachment() {
        return attachment;
    }

    public int getInterestOps() {
        return interestOps;
    }

    public int getReadyOps() {
        return readyOps;
    }
}
