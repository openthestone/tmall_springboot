package com.interceptor;

import com.pojo.User;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        HttpSession session = httpServletRequest.getSession();
        String contextPath=session.getServletContext().getContextPath();
        //存放需要登陆才能访问的路径
        String[] requireAuthPages = new String[]{"home","square","non_standard","gen","api"};
        String uri = httpServletRequest.getRequestURI();
        //去掉前缀/tmall_springboot
        uri = StringUtils.remove(uri, contextPath+"/");
        String page = uri;
        //判断是否是以 requireAuthPages 里的开头的
        if(begingWith(page, requireAuthPages)){
            User user = (User) session.getAttribute("user");
            if(user==null) {
                httpServletResponse.sendRedirect("login");
                return false;
            }
        }
        return true;
    }

    private boolean begingWith(String page, String[] requireAuthPages) {
        boolean result = false;
        for (String requiredAuthPage : requireAuthPages) {
            if(StringUtils.startsWith(page, requiredAuthPage)) {
                result = true;
                break;
            }
        }
        return result;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
