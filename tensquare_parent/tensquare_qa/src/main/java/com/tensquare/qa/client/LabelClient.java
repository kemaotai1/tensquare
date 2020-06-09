package com.tensquare.qa.client;

import entity.Result;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 远程调用接口
 * http://127.0.0.1:9001//label/10
 */
@FeignClient(value = "tensquare-base",fallback = LabelClientImpl.class)   //对方的微服务名称，注意：该微服务名称在Eureka发现对应的地址
public interface LabelClient {

    /**
     * 查询一个
     */
    @RequestMapping(value = "/label/{id}",method = RequestMethod.GET)
    public Result findById(@PathVariable("id") String id);

}
