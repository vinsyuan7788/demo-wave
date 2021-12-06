package com.demo.wave.test.unit.channel.socket.server.utility;

import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;

/**
 * @author Vince Yuan
 * @date 2021/12/6
 */
public class SelectionKeyInfo {

    private SocketChannel acceptedSocketChannel;
    private Selector selector;
    private SelectionKey selectedKey;
    private int readyOps;
    private int interestOps;
    private Object attachment;

    public SelectionKeyInfo(SocketChannel acceptedSocketChannel, Selector selector, SelectionKey selectedKey, int readyOps, int interestOps, Object attachment) {
        this.acceptedSocketChannel = acceptedSocketChannel;
        this.selector = selector;
        this.selectedKey = selectedKey;
        this.readyOps = readyOps;
        this.interestOps = interestOps;
        this.attachment = attachment;
    }

    public SocketChannel getAcceptedSocketChannel() {
        return acceptedSocketChannel;
    }

    public Selector getSelector() {
        return selector;
    }

    public SelectionKey getSelectedKey() {
        return selectedKey;
    }

    public int getReadyOps() {
        return readyOps;
    }

    public int getInterestOps() {
        return interestOps;
    }

    public Object getAttachment() {
        return attachment;
    }
}
