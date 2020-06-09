package com.tensquare.user.filter;

import io.jsonwebtoken.Claims;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import util.JwtUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Jwt权限拦截器
 */
@Component
public class JwtFilter extends HandlerInterceptorAdapter{

    @Autowired
    private JwtUtil jwtUtil;


    /**
     * 在Controller的方法执行之前调用
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //1.判断请求头是否存在Authorization
        String auth = request.getHeader("Authorization");

        System.out.println("接收的头信息："+auth);

        if(auth!=null){
            //2.判断是否以Bearer开头
            if(auth.startsWith("Bearer")){
                //3.截取Token字符串
                String token = auth.substring(7);

                //4.使用jjwt解析token的合法性
                Claims claims = jwtUtil.parseJWT(token);

                if(claims!=null){

                    //5.1.判断是否为管理员角色
                    if(claims.get("roles").equals("admin")){
                        request.setAttribute("admin_claims",claims);
                    }

                    //5.1 判断是否为普通用户
                    if(claims.get("roles").equals("user")){
                        request.setAttribute("user_claims",claims);
                    }

                }
            }


        }


        return true;
    }
}
