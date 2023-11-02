package com.example.randomcoffee.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

//@Component
//@Order(Ordered.HIGHEST_PRECEDENCE)
//public class SimpleCORSFilter implements Filter {
//
////    @Override
////    public void init(FilterConfig filterConfig) throws ServletException {
////
////    }
//
//    public static final List<String> URLS = List.of(
//            "https://ya.ru",
//            "http://localhost:8090"
//    );
//
//    @Override
//    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
//        HttpServletResponse response = (HttpServletResponse) servletResponse;
//        HttpServletRequest request = (HttpServletRequest) servletRequest;
//
//        String origin = request.getHeader("Origin");
//
//        if (origin == null) {
//            response.setHeader("Access-Control-Allow-Origin", "*");
//        } else if (URLS.contains(origin)) {
//            response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
//        }
//
//        response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE, PUT");
//        response.setHeader("Access-Control-Max-Age", "12000");
//        response.setHeader("Access-Control-Allow-Headers", "*");
//        response.setHeader("Access-Control-Expose-Headers", "*");
//
//        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
//            response.setStatus(HttpServletResponse.SC_OK);
//        } else {
//            filterChain.doFilter(servletRequest, servletResponse);
//        }
//    }
//
//    @Override
//    public void destroy() {
//
//    }
//}
