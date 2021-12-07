package com.demo.wave.test.unit.classloader;

import com.demo.wave.test.unit.classloader.loader.WebApplicationClassLoader;
import com.sun.javafx.binding.Logging;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;

/**
 * @author Vince Yuan
 * @date 2021/12/7
 */
public class ClassLoaderTest {

    private static final Logger LOG = LoggerFactory.getLogger(ClassLoaderTest.class);

    @Before
    public void testPreparation() {
        System.out.println("testPreparation is completed");
    }

    @Test
    public void test1() {
        // Bootstrap class loader
        ClassLoader booleanClassLoader = findClassLoader(Boolean.class);
        ClassLoader byteClassLoader = findClassLoader(Byte.class);
        ClassLoader shortClassLoader = findClassLoader(Short.class);
        ClassLoader characterClassLoader = findClassLoader(Character.class);
        ClassLoader integerClassLoader = findClassLoader(Integer.class);
        ClassLoader floatClassLoader = findClassLoader(Float.class);
        ClassLoader longClassLoader = findClassLoader(Long.class);
        ClassLoader doubleClassLoader = findClassLoader(Double.class);
        ClassLoader stringClassLoader = findClassLoader(String.class);
        LOG.info("Boolean class loader: {}", toString(booleanClassLoader));
        LOG.info("Byte class loader: {}", toString(byteClassLoader));
        LOG.info("Short class loader: {}", toString(shortClassLoader));
        LOG.info("Character class loader: {}", toString(characterClassLoader));
        LOG.info("Integer class loader: {}", toString(integerClassLoader));
        LOG.info("Float class loader: {}", toString(floatClassLoader));
        LOG.info("Long class loader: {}", toString(longClassLoader));
        LOG.info("Double class loader: {}", toString(doubleClassLoader));
        LOG.info("String class loader: {}", toString(stringClassLoader));

        // Extension class loader
        ClassLoader loggingClassLoader = findClassLoader(Logging.class);
        LOG.info("Logging class loader: {}", toString(loggingClassLoader));

        // System class loader
        ClassLoader loggerClassLoader = findClassLoader(Logger.class);
        ClassLoader loggerFactoryClassLoader = findClassLoader(LoggerFactory.class);
        LOG.info("Logger class loader: {}", toString(loggerClassLoader));
        LOG.info("LoggerFactory class loader: {}", toString(loggerFactoryClassLoader));

        System.out.println("testCheckingClassLoader is completed");
    }

    @Test
    public void test2() throws Exception {

        // Set thread context class loader (TCCL)
        URL[] urls = new URL[] {
                new URL("file:D:/Software/Engineering/MyBatis/ReverseEngineering/mysql-connector-java-8.0.23.jar")
        };
        WebApplicationClassLoader webApplicationClassLoader = new WebApplicationClassLoader(urls);
        Thread.currentThread().setContextClassLoader(webApplicationClassLoader);

        // Get thread context class loader (TCCL)
        ClassLoader threadContextClassLoader = Thread.currentThread().getContextClassLoader();
        LOG.info("Thread context class loader: {}", toString(threadContextClassLoader));
        Class<?> driverClass = threadContextClassLoader.loadClass("com.mysql.cj.jdbc.Driver");
        LOG.info("Driver class: {}", driverClass);
        Object driverInstance = driverClass.newInstance();
        LOG.info("Driver instance: {}", driverInstance);

        System.out.println("testTCCL is completed");
    }

    @After
    public void testCompletion() {
        System.out.println("testCompletion is completed");
    }

    /**
     * This method is used to find the class loader that loads specific class
     *
     * @param clazz
     * @return
     */
    private ClassLoader findClassLoader(Class<?> clazz) {
        return clazz.getClassLoader();
    }

    /**
     * This method is used to get the class loader string representation
     *
     * @param classLoader
     * @return
     */
    private String toString(ClassLoader classLoader) {
        if (classLoader == null) {
            return "BootstrapClassLoader";
        } else {
            StringBuffer sb = new StringBuffer();
            String classLoaderString = classLoader.toString();
            if (classLoaderString.contains("ExtClassLoader")){
                sb.append("ExtensionClassLoader[");
            } else if (classLoaderString.contains("AppClassLoader")) {
                sb.append("SystemClassLoader[");
            } else {
                sb.append("UserClassLoader[");
            }
            sb.append("current=").append(classLoaderString).append(";");
            sb.append("parent=").append(classLoader.getParent());
            sb.append("]");
            return sb.toString();
        }
    }
}
