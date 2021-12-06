package com.demo.wave.test.unit.channel.socket.server;

import com.demo.wave.test.unit.channel.socket.server.poller.Poller;
import com.demo.wave.test.unit.channel.socket.server.poller.Poller2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousCloseException;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * This class is used to serve as a server that accepts connection from clients
 *
 * @author Vince Yuan
 * @date 2021/12/5
 */
public class Server extends Thread {

    private static final Logger LOG = LoggerFactory.getLogger(Server.class);

    private ServerSocketChannel serverSocketChannel;
    private Poller[] pollers;
    private final AtomicInteger timesOfGetPoller;

    public Server() throws Exception {
        pollers = new Poller[Runtime.getRuntime().availableProcessors()];
        for (int i = 0; i < pollers.length; i++) {
            // It is proved that the 2nd way works through testing
            pollers[i] = new Poller2();
            pollers[i].start();
        }
        timesOfGetPoller = new AtomicInteger(0);
        serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.configureBlocking(true);
        serverSocketChannel.bind(new InetSocketAddress(8585));
    }

    @Override
    public void run() {
        while (serverSocketChannel.isOpen()) {
            try {
                SocketChannel acceptedSocketChannel = serverSocketChannel.accept();
                LOG.info("Server accepts connection from client: {}", acceptedSocketChannel.getRemoteAddress());
                acceptedSocketChannel.configureBlocking(false);
                getPoller().register(acceptedSocketChannel);
            } catch (Throwable t) {
                if (t instanceof AsynchronousCloseException || t instanceof ClosedChannelException) {
                    // Do nothing here
                } else {
                    t.printStackTrace();
                }
            }
        }
    }

    public void close() throws Exception {
        if (serverSocketChannel.socket().isClosed()) { return; }
        serverSocketChannel.socket().close();
        serverSocketChannel.close();
        join();
    }

    private Poller getPoller() {
        return pollers[timesOfGetPoller.getAndIncrement() % pollers.length];
    }
}
