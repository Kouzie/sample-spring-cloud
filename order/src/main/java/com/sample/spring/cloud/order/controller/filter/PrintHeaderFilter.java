package com.sample.spring.cloud.order.controller.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Slf4j
@Component
public class PrintHeaderFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        List<String> headerNames = Collections.list(httpServletRequest.getHeaderNames());
        log.info("requestUrl:" + httpServletRequest.getRequestURL());
        for (String headerName : headerNames) {
            log.info("requestHeader:" + headerName + ":" + httpServletRequest.getHeader(headerName));
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}