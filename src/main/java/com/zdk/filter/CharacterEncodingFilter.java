package com.zdk.filter;

import javax.servlet.*;
import java.io.IOException;

/**
 * @author zdk
 * @date 2021/3/18 19:26
 */
public class CharacterEncodingFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        //web服务器启动时  filter就初始化了
        System.out.println("CharacterEncodingFilter已经初始化");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        //chain: 链
        servletRequest.setCharacterEncoding("utf-8");
        servletResponse.setCharacterEncoding("utf-8");
        servletResponse.setContentType("text/html;charset=utf-8");

        //让请求继续执行 如果不写 请求到这里就直接被拦截了
        filterChain.doFilter(servletRequest,servletResponse);

    }

    @Override
    public void destroy() {
        //web服务器关闭时 filter过滤器才销毁
        System.out.println("CharacterEncodingFilter已经销毁");
    }
}
