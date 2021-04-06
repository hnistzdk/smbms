package com.zdk.filter;

import com.zdk.pojo.User;
import com.zdk.util.Constants;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author zdk
 * @date 2021/3/20 18:49
 *
 * 权限拦截  未登录时不能访问登录后页面
 */
public class SysFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request= (HttpServletRequest) servletRequest;
        HttpServletResponse response= (HttpServletResponse) servletResponse;
        //从session中获取用户
        User user=(User) request.getSession().getAttribute(Constants.USER_SESSION);
        if(user==null){
            //已经被移除或注销或未登录
            response.sendRedirect("/error.jsp");
        }
        filterChain.doFilter(servletRequest,servletResponse);
    }

    @Override
    public void destroy() {

    }
}
