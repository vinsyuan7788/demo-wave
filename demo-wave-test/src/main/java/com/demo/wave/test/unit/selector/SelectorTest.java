package com.demo.wave.test.unit.selector;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

/**
 * @author Vince Yuan
 * @date 2021/12/5
 */
public class SelectorTest {

    private static final Logger LOG = LoggerFactory.getLogger(SelectorTest.class);

    @Test
    public void test1() throws Exception {
        Selector selector = Selector.open();
        CountDownLatch countDownLatch = new CountDownLatch(1);
        ServerSocketChannel ssc = ServerSocketChannel.open();
        ssc.configureBlocking(false);
        ssc.register(selector, SelectionKey.OP_ACCEPT);
        while (true) {
            selector.select(1000);
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            LOG.info("Selection keys: {}", selectionKeys);
//            countDownLatch.await();
        }
    }

    @Test
    public void test2() throws Exception {
        boolean flag = (5 & 1) != 0;
        System.out.println(flag);
    }
}
