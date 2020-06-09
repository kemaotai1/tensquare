package com.tensquare.manage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;
import util.JwtUtil;

/**
 * 管理员后台 微服务网关
 */
@SpringBootApplication
@EnableEurekaClient
@EnableZuulProxy
public class ManageApplication {

    public static void main(String[] args) {
        SpringApplication.run(ManageApplication.class,args);
    }

    @Bean
    public JwtUtil jwtUtil(){
        return new JwtUtil();
    }
}
