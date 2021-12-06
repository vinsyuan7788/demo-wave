package com.demo.wave.test.integration.comparison.hiama;

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

/**
 * The result of this test can be compared with "MultipleClientTest" in "haima" project
 *
 * @author Vince Yuan
 * @date 2021/12/5
 */
public class MultipleClientTest {

    private DemoServer server;
    private DemoClient[] clients;

    private Object[] dataArrayToWrite = new Object[] { 12345, 54321L, "Hello NIO" };
    private Random random = new Random();

    @Before
    public void testStartServerAndClients() {
        server = new ReactorDemoServer(8686, ProcessingType.MIDDLE_WARE);
        server.startServer();
        while (server.getServerState() != ServerState.RUNNING) { }
        System.out.println("server is started");
        clients = new NioDemoClient[2];
        for (int i = 0; i < clients.length; i++) {
            clients[i] = new NioDemoClient(new InetSocketAddress(8686));
            clients[i].startClient();
            while (clients[i].getClientState() != ClientState.RUNNING) { }
        }
        System.out.println("clients are started");
        System.out.println("testStartServerAndClients is completed");
    }

    @Test
    public void test1() throws Exception {
        List<Boolean> successList = new ArrayList<>();

        long startTimeMillis = System.currentTimeMillis();
        for (int i = 0; i < clients.length; i++) {
            boolean success = clients[i].write(dataArrayToWrite[random.nextInt(dataArrayToWrite.length)]);
            successList.add(success);
        }
        System.out.println("Time elapsed for one-time communication: " + (System.currentTimeMillis() - startTimeMillis) + "ms | Success: " + successList);

        System.out.println("testClientConnection is completed");
    }

//    @Test
//    public void test2() throws Exception {
//        client = new NioDemoClient(new InetSocketAddress(8686));
//        client.startClient();
//        while (client.getClientState() != ClientState.RUNNING) { }
//
//        List<Boolean> resultList = new ArrayList<>();
//        long startTimeMillis = System.currentTimeMillis();
//        for (int i = 0; i < 10000; i++) {
//            Boolean result = client.write(dataArrayToWrite[random.nextInt(dataArrayToWrite.length)]);
//            resultList.add(result);
//        }
//        // ~10ms for 100 communications
//        System.out.println("Time elapsed for highly-intensive communication: " + (System.currentTimeMillis() - startTimeMillis) + "ms" +
//                " | result size: " + resultList.size() + " | result: " + resultList);
//
//        client.closeClient();
//        System.out.println("testIntensiveCommunication is completed");
//    }
//
//    @Test
//    public void test3() throws Exception {
//        client = new NioDemoClient(new InetSocketAddress(8686));
//        client.startClient();
//        while (client.getClientState() != ClientState.RUNNING) { }
//
//        List<Long> resultList = new ArrayList<>();
//        long startTimeMillis = System.currentTimeMillis();
//        for (int i = 0; i < 10000; i++) {
//            Long snowflakeId = client.getSnowflakeId(3);
//            resultList.add(snowflakeId);
//        }
//        // ~10ms for 100 communications
//        System.out.println("Time elapsed to get snowflake ID: " + (System.currentTimeMillis() - startTimeMillis) + "ms" +
//                " | result size: " + resultList.stream().distinct().count() + " | result: " + resultList.stream().distinct().collect(Collectors.toList()));
//
//        client.closeClient();
//        System.out.println("testGetSnowflakeId is completed");
//    }

    @After
    public void testCloseServerAndClients() throws Exception {
        Thread.sleep(1000);
        for (int i = 0; i < clients.length; i++) {
            clients[i].closeClient();
        }
        System.out.println("clients are closed");
        Thread.sleep(1000);
        server.closeServer();
        System.out.println("server is closed");
        System.out.println("testCloseServerAndClients is completed");
    }
}
