package com.demo.wave.fundamental.server.business.web.utility.filter;

import com.demo.wave.fundamental.server.business.web.utility.servlet.BaseServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.FilterChain;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

/**
 * @author Vince Yuan
 * @date 2021/12/1
 */
public class BaseFilterChain implements FilterChain {

    private static final Logger LOG = LoggerFactory.getLogger(BaseFilterChain.class);

    private Servlet servlet;

    public BaseFilterChain() {
        try {
            this.servlet = new BaseServlet();
        } catch (Throwable t) {
            handleInitializingThrowable(t);
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response) throws IOException, ServletException {
        LOG.info("BaseFilterChain | request: {} | response: {}", request, response);
        servlet.service(request, response);
    }

    private void handleInitializingThrowable(Throwable t) {
        LOG.error("create filter chain error", t);
    }
}
