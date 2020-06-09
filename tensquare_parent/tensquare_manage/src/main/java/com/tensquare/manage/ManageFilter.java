package com.tensquare.manage;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import com.netflix.zuul.exception.ZuulException;
import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import util.JwtUtil;

import javax.servlet.http.HttpServletRequest;

/**
 * 管理员后台网关过滤器
 */
@Component
public class ManageFilter extends ZuulFilter{
    @Override
    public String filterType() {
        return "pre";
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Autowired
    private JwtUtil jwtUtil;


    @Override
    public Object run() throws ZuulException {


        //1.判断是否存在Authorization头信息
        RequestContext requestContext = RequestContext.getCurrentContext();
        HttpServletRequest request = requestContext.getRequest();
        String auth = request.getHeader("Authorization");

        //单独对某些请求进行放行
        String uri = request.getRequestURI();
        if(uri.contains("/admin/login") ){
            //放行
            return null;
        }


        if(auth!=null){
            //2.判断是否以Bearer开头
            if(auth.startsWith("Bearer")){
                //3.截取token信息
                String token = auth.substring(7);

                //4.使用jjwt校验token合法性
                Claims claims = jwtUtil.parseJWT(token);

                if(claims!=null){

                    //5.判断是否为管理员身份
                    if(claims.get("roles").equals("admin")){
                        //是管理员，放行
                        return null;
                    }

                }

            }



        }


        //不是管理员，中断请求，返回提示信息给用户
        requestContext.setSendZuulResponse(false); // 中断请求（到此为止）
        //返回信息给用户
        requestContext.setResponseBody("你不是管理员，请先登录");
        //设置内容的编码
        requestContext.getResponse().setContentType("text/html;charset=utf-8");


        return null;
    }
}
