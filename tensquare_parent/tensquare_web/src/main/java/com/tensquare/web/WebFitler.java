package com.tensquare.web;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * 用户前台网关过滤器
 */
@Component
public class WebFitler extends ZuulFilter{

    /**
     * 过滤器类型
     *   pre: 在执行网关转发之前
     *   route: 在执行网关转发的时候
     *   post: 在执行网关转发之后
     *   error: 在route或post错误的时候
     * @return
     */
    @Override
    public String filterType() {
        return "pre";
    }

    /**
     * 过滤器顺序
     *   数值越大，优先级越低
     * @return
     */
    @Override
    public int filterOrder() {
        return 0;
    }

    /**
     * 是否执行过滤
     * @return
     */
    @Override
    public boolean shouldFilter() {
        return true;
    }

    /**
     * 过滤器的核心逻辑
     * @return
     * @throws ZuulException
     */
    @Override
    public Object run() throws ZuulException {
        System.out.println("执行前台用户的网关过滤器...");

        //1.从用户请求取出Authorization头信息
        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();
        String auth = request.getHeader("Authorization");

        //2.把头信息主动放入到zuul的请求中
        if(auth!=null && !auth.equals("")){
            requestContext.addZuulRequestHeader("Authorization",auth);
        }

        //return null就代表放行该请求
        return null;
    }
}
