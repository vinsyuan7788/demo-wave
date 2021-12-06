package com.demo.wave.fundamental.server.business.web.utility.servlet;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Vince Yuan
 * @date 2021/12/1
 */
public class BaseServlet extends HttpServlet {

    private static final Logger LOG = LoggerFactory.getLogger(BaseServlet.class);

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LOG.info("BaseServlet | request: {} | response: {}", req, resp);
        super.service(req, resp);
    }
}
