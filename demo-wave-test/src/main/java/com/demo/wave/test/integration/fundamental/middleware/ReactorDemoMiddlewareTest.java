package com.demo.wave.test.integration.fundamental.middleware;

import com.demo.wave.fundamental.client.nonblocking.DemoClient;
import com.demo.wave.fundamental.client.nonblocking.NioDemoClient;
import com.demo.wave.fundamental.server.design.reactor.DemoServer;
import com.demo.wave.fundamental.server.design.reactor.ReactorDemoServer;
import com.demo.wave.fundamental.server.design.reactor.processor.utility.ProcessingType;
import com.demo.wave.fundamental.utility.state.client.ClientState;
import com.demo.wave.fundamental.utility.state.server.reactor.ServerState;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * @author Vince Yuan
 * @date 2021/12/1
 */
public class ReactorDemoMiddlewareTest {

    private DemoServer server;
    private DemoClient client;

    private Object[] dataArrayToWrite = new Object[] { 12345, 54321L, "Hello NIO" };
    private Random random = new Random();

    @Before
    public void testStartServer() {
        server = new ReactorDemoServer(8686, ProcessingType.MIDDLE_WARE);
        server.startServer();
        while (server.getServerState() != ServerState.RUNNING) { }
        System.out.println("testStartServer is completed");
    }

    @Test
    public void test1() throws Exception {
        client = new NioDemoClient(new InetSocketAddress(8686));
        client.startClient();
        while (client.getClientState() != ClientState.RUNNING) { }

        long startTimeMillis = System.currentTimeMillis();
        boolean success = client.write(dataArrayToWrite[random.nextInt(dataArrayToWrite.length)]);
        System.out.println("Time elapsed for one-time communication: " + (System.currentTimeMillis() - startTimeMillis) + "ms | Success: " + success);

        client.closeClient();
        System.out.println("testClientConnection is completed");
    }

    @Test
    public void test2() throws Exception {
        client = new NioDemoClient(new InetSocketAddress(8686));
        client.startClient();
        while (client.getClientState() != ClientState.RUNNING) { }

        List<Boolean> resultList = new ArrayList<>();
        long startTimeMillis = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
            Boolean result = client.write(dataArrayToWrite[random.nextInt(dataArrayToWrite.length)]);
            resultList.add(result);
        }
        // ~10ms for 100 communications
        System.out.println("Time elapsed for highly-intensive communication: " + (System.currentTimeMillis() - startTimeMillis) + "ms" +
                " | result size: " + resultList.size() + " | result: " + resultList);

        client.closeClient();
        System.out.println("testIntensiveCommunication is completed");
    }

    @Test
    public void test3() throws Exception {
        client = new NioDemoClient(new InetSocketAddress(8686));
        client.startClient();
        while (client.getClientState() != ClientState.RUNNING) { }

        List<Long> resultList = new ArrayList<>();
        long startTimeMillis = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
            Long snowflakeId = client.getSnowflakeId(3);
            resultList.add(snowflakeId);
        }
        // ~10ms for 100 communications
        System.out.println("Time elapsed to get snowflake ID: " + (System.currentTimeMillis() - startTimeMillis) + "ms" +
                " | result size: " + resultList.stream().distinct().count() + " | result: " + resultList.stream().distinct().collect(Collectors.toList()));

        client.closeClient();
        System.out.println("testGetSnowflakeId is completed");
    }

    @After
    public void testCloseServer() throws Exception {
        Thread.sleep(1000);
        server.closeServer();
        System.out.println("testCloseServer is completed");
    }
}
