package com.yumumu.syncServer.filter;

import java.io.IOException;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

/**
 * @author zhanghailin
 * @date 2022/6/7
 */
@Component
public class MyFilter implements Filter {

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
        throws IOException, ServletException {
        System.out.println(((HttpServletRequest)servletRequest).getRequestURI());
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
