package com.demo.wave.fundamental.server.design.reactor;

import com.demo.wave.common.utility.LogUtils;
import com.demo.wave.fundamental.server.design.reactor.acceptor.DemoAcceptor;
import com.demo.wave.fundamental.server.design.reactor.acceptor.ReactorDemoAcceptor;
import com.demo.wave.fundamental.server.business.middleware.utility.snowflake.factory.SnowFlakeIdWorkerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Vince Yuan
 * @date 2021/11/29
 */
public class ReactorDemoServer extends DemoServer {

    private static final Logger LOG = LoggerFactory.getLogger(ReactorDemoServer.class);

    private DemoAcceptor acceptor;

    public ReactorDemoServer(int portToBind, int processingType){
        this.acceptor = new ReactorDemoAcceptor(this, portToBind, processingType);
    }

    @Override
    public void startServer() {
        SnowFlakeIdWorkerFactory.init(5, 10, 2);
        acceptor.startAcceptor();
        LOG.info(LogUtils.getMessage("Server is started"));
    }

    @Override
    public void closeServer() {
        acceptor.closeAcceptor();
        LOG.info(LogUtils.getMessage("Server is closed"));
    }
}
