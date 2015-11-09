package com.technology.jep.jepria.server.cors;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

public class CORSFilter implements Filter {
  static Logger logger = Logger.getLogger(CORSFilter.class.getName());

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    	logger.info("CORSFilter initiated");
    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse,
                         FilterChain chain) throws IOException, ServletException {
    	logger.info("CORSFilter doFilter() BEGIN");

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        
    	logger.trace("CORSFilter.doFilter(): request.getMethod() = " + request.getMethod());
    	logger.trace("CORSFilter.doFilter(): request.getContextPath() = " + request.getContextPath());
    	logger.trace("CORSFilter.doFilter(): request.getAuthType() = " + request.getAuthType());
    	logger.trace("CORSFilter.doFilter(): request.getPathInfo() = " + request.getPathInfo());
    	logger.trace("CORSFilter.doFilter(): request.getRequestURI() = " + request.getRequestURI());
    	logger.trace("CORSFilter.doFilter(): request.getRequestURL() = " + request.getRequestURL());
    	logger.trace("CORSFilter.doFilter(): request.getQueryString() = " + request.getQueryString());
    	logger.trace("CORSFilter.doFilter(): request.getServletPath()  " + request.getServletPath());

        // Just ACCEPT and REPLY OK if OPTIONS
        HttpServletResponse resp = (HttpServletResponse) servletResponse;
        resp.addHeader("Access-Control-Allow-Origin","*");
//        resp.addHeader("Access-Control-Allow-Methods","GET,POST");
        resp.addHeader("Access-Control-Allow-Methods","GET,POST,OPTIONS");
        resp.addHeader("Access-Control-Allow-Headers","Origin, X-Requested-With, Content-Type, Accept");

        String method = request.getMethod();
    	logger.debug("CORSFilter.doFilter(): method = " + method);
    	
        if(method.equals("OPTIONS")) {
            resp.setStatus(HttpServletResponse.SC_OK);
            return;
        }
        
        chain.doFilter(request, servletResponse);
        
    	logger.info("CORSFilter doFilter() END");
    }

    @Override
    public void destroy() {
    	logger.info("CORSFilter destroyed");
    }
} 