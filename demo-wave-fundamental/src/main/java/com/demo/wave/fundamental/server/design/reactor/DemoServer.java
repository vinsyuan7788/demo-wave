package com.demo.wave.fundamental.server.design.reactor;

import com.demo.wave.fundamental.utility.state.server.reactor.ServerState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Vince Yuan
 * @date 2021/12/1
 */
public abstract class DemoServer {

    private static final Logger LOG = LoggerFactory.getLogger(DemoServer.class);

    private ServerState serverState;

    public void setServerState(ServerState serverState) {
        this.serverState = serverState;
        LOG.info("Server state is set to {}", serverState);
    }

    public ServerState getServerState() {
        return serverState;
    }

    /**
     * This method is used to start the server
     */
    public abstract void startServer();

    /**
     * This method is used to close the server
     */
    public abstract void closeServer();
}
