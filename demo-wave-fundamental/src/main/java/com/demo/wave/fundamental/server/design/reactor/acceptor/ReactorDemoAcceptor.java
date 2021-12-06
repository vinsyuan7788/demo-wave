package com.demo.wave.fundamental.server.design.reactor.acceptor;

import com.demo.wave.fundamental.server.design.reactor.DemoServer;
import com.demo.wave.fundamental.server.design.reactor.poller.DemoPoller;
import com.demo.wave.fundamental.server.design.reactor.poller.ReactorDemoPoller;
import com.demo.wave.fundamental.utility.auxiliary.ContainerRunner;
import com.demo.wave.fundamental.utility.state.server.reactor.AcceptorState;
import com.demo.wave.fundamental.utility.state.server.reactor.ServerSocketState;
import com.demo.wave.fundamental.utility.state.server.reactor.ServerState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.nio.channels.AsynchronousCloseException;
import java.nio.channels.ClosedByInterruptException;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * This class is used to serve as a main reactor that accepts incoming connections from request
 *
 * @author Vince Yuan
 * @date 2021/11/29
 */
public class ReactorDemoAcceptor extends ContainerRunner implements Runnable, DemoAcceptor {

    private static final Logger LOG = LoggerFactory.getLogger(ReactorDemoAcceptor.class);

    private DemoServer server;
    private ContainerRunner acceptorThread;
    private DemoPoller[] pollers;
    /**
     * This field is used to specify the number of poller, which is 2 in minimum by default
     */
    private int numberOfPoller = Math.min(2, Runtime.getRuntime().availableProcessors());
    /**
     * This field is sued to specify the times to get a poller, which is 0 by default
     */
    private AtomicInteger timesOfGetPoller = new AtomicInteger(0);
    private int processingType;

    private ServerSocketChannel serverSocketChannel;

    private ServerSocketState serverSocketState;
    private AcceptorState acceptorState;

    public ReactorDemoAcceptor(DemoServer server, int portToBind, int processingType) {
        try {
            this.server = server;
            this.server.setServerState(ServerState.INITIAL);

            acceptorThread = new ContainerRunner(this, "Acceptor");
            pollers = new ReactorDemoPoller[numberOfPoller];
            this.processingType = processingType;

            setServerSocketState(ServerSocketState.INITIAL);
            serverSocketChannel = ServerSocketChannel.open();
            LOG.info("Server socket channel is opened: {}", serverSocketChannel);
            serverSocketChannel.configureBlocking(true);
            LOG.info("Server socket channel is configured to {}", getSelectableChannelBlockingMode(serverSocketChannel));
            serverSocketChannel.bind(new InetSocketAddress(portToBind));
            LOG.info("Server socket channel is bound to port: {}", portToBind);
            setServerSocketState(ServerSocketState.BOUND);

            setAcceptorState(AcceptorState.INITIAL);
        } catch (Throwable t) {
            handleInitializingThrowable(t);
        }
    }

    @Override
    public void run() {
        server.setServerState(ServerState.RUNNING);
        setServerSocketState(ServerSocketState.RUNNING);
        setAcceptorState(AcceptorState.RUNNING);
        while (serverSocketState == ServerSocketState.RUNNING && acceptorState == AcceptorState.RUNNING) {
            try {
                SocketChannel acceptedSocketChannel = serverSocketChannel.accept();
                LOG.info("Server accepts connection from client: {}", acceptedSocketChannel.getRemoteAddress());
                configureSocketChannel(acceptedSocketChannel);
                getPoller().register(acceptedSocketChannel);
            } catch (Throwable t) {
                handleRunningThrowable(t);
            }
        }
    }

    @Override
    public void configureSocketChannel(SocketChannel socketChannel) {
        try {
            socketChannel.configureBlocking(false);
        } catch (Throwable t) {
            handleConfiguringSocketChannelThrowable(t);
        }
    }

    @Override
    public void handleConfiguringSocketChannelThrowable(Throwable t) {
        LOG.error("Configure socket channel error", t);
    }

    @Override
    public void startAcceptor() {
        try {
            for (int i = 0; i < pollers.length; i++) {
                pollers[i] = new ReactorDemoPoller("Poller" + (i + 1), processingType);
                pollers[i].startPoller();
            }
            acceptorThread.start();
        } catch (Throwable t) {
            handleStartingThrowable(t);
        }
    }

    @Override
    public void closeAcceptor() {
        try {
            for (int i = 0; i < pollers.length; i++) {
                pollers[i].closePoller();
            }
            setServerSocketState(ServerSocketState.CLOSED);
            setAcceptorState(AcceptorState.CLOSED);
            acceptorThread.interrupt();
            acceptorThread.join();
            if (!serverSocketChannel.socket().isClosed()) {
                serverSocketChannel.socket().close();
                serverSocketChannel.close();
            }
            server.setServerState(ServerState.CLOSED);
        } catch (Throwable t) {
            handleClosingThrowable(t);
        }
    }

    @Override
    public void handleInitializingThrowable(Throwable t) {
        LOG.error("Create acceptor error", t);
    }

    @Override
    public void handleStartingThrowable(Throwable t) {
        LOG.error("Start acceptor error", t);
    }

    @Override
    public void handleClosingThrowable(Throwable t) {
        LOG.error("Close acceptor error", t);
    }

    @Override
    public void handleRunningThrowable(Throwable t) {
        if (t instanceof ClosedByInterruptException || t instanceof AsynchronousCloseException) {
            // It is totally fine here since it happens when we close the acceptor, where the server socket needs to be
            // interrupted (which raises ClosedByInterruptException) or closed (which raises AsynchronousCloseException)
            // to stop the blocking-mode operation "accept", therefore this exception is tolerable here.
        } else {
            LOG.error("Running acceptor error", t);
        }
    }

    /****************************** State Machine ******************************/

    public void setServerSocketState(ServerSocketState serverSocketState) {
        this.serverSocketState = serverSocketState;
        LOG.info("Server socket state is set to {}", serverSocketState);
    }

    public ServerSocketState getServerSocketState() {
        return serverSocketState;
    }

    public void setAcceptorState(AcceptorState acceptorState) {
        this.acceptorState = acceptorState;
        LOG.info("Acceptor state is set to {}", acceptorState);
    }

    public AcceptorState getAcceptorState() {
        return acceptorState;
    }

    /****************************** Utility Method ******************************/

    /**
     * This method is used to get a poller.
     * So far the pool will be returned in a round-bin way
     *
     * @return
     */
    private DemoPoller getPoller() {
        return pollers[Math.abs(timesOfGetPoller.getAndIncrement() % pollers.length)];
    }
}
