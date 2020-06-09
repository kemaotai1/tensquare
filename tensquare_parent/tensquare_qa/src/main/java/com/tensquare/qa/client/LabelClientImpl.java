package com.tensquare.qa.client;

import entity.Result;
import entity.StatusCode;
import org.springframework.stereotype.Component;

/**
 * 熔断器本地实现
 */
@Component
public class LabelClientImpl implements LabelClient{

    @Override
    public Result findById(String id) {
        return new Result(true, StatusCode.OK,"临时数据，代表正常服务不可用!!!");
    }
}
