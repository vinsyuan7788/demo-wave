package com.demo.wave.test.unit.classloader.loader;

import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandlerFactory;

/**
 * @author Vince Yuan
 * @date 2021/12/7
 */
public class WebApplicationClassLoader extends URLClassLoader {

    public WebApplicationClassLoader(URL[] urls, ClassLoader parent) {
        super(urls, parent);
    }

    public WebApplicationClassLoader(URL[] urls) {
        super(urls);
    }

    public WebApplicationClassLoader(URL[] urls, ClassLoader parent, URLStreamHandlerFactory factory) {
        super(urls, parent, factory);
    }
}
