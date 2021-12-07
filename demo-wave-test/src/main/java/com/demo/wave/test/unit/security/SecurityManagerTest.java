package com.demo.wave.test.unit.security;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.AccessController;

/**
 * {@link SecurityManager} serves the same purpose basically with {@link AccessController}
 * since the Java Security Model (JSM) is involving with the Java version updating
 *
 * @author Vince Yuan
 * @date 2021/12/7
 */
public class SecurityManagerTest {

    private static final Logger LOG = LoggerFactory.getLogger(SecurityManagerTest.class);

    private SecurityManager securityManager;
    private Object securityContext;

    @Before
    public void testPreparation() {
        securityManager = System.getSecurityManager();
        LOG.info("Security manager: {}", securityManager);
        securityContext = System.getSecurityManager().getSecurityContext();
        LOG.info("Security context: {}", securityContext);

        System.out.println("testPreparation is completed");
    }

    @Test
    public void test1() {

    }

    @After
    public void testCompletion() {
        System.out.println("testCompletion is completed");
    }
}
