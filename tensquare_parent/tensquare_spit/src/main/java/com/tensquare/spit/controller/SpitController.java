package com.tensquare.spit.controller;

import com.tensquare.spit.pojo.Spit;
import com.tensquare.spit.service.SpitService;
import entity.PageResult;
import entity.Result;
import entity.StatusCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * 吐槽controller
 */
@RestController    // @RestController= @Controller+@ResponseBody
@RequestMapping("/spit")
@CrossOrigin //解决跨域问题 jsonp
public class SpitController {

    @Autowired
    private SpitService spitService;


    /**
     * 查询所有
     */
    @RequestMapping(method = RequestMethod.GET)
    public Result findAll(){
        return new Result(true, StatusCode.OK,"查询成功",spitService.findAll());
    }

    /**
     * 查询一个
     */
    @RequestMapping(value = "/{id}",method = RequestMethod.GET)
    public Result findById(@PathVariable String id){
        return new Result(true,StatusCode.OK,"查询成功",spitService.findById(id));
    }

    /**
     * 添加
     */
    @RequestMapping(method = RequestMethod.POST)
    public Result add(@RequestBody Spit spit){
        spitService.add(spit);
        return new Result(true,StatusCode.OK,"添加成功");
    }

    /**
     * 修改
     */
    @RequestMapping(value = "/{id}",method = RequestMethod.PUT)
    public Result update(@PathVariable String id,@RequestBody Spit spit){
        //设置id
        spit.setId(id);
        spitService.update(spit);
        return new Result(true,StatusCode.OK,"修改成功");
    }

    /**
     * 删除
     */
    @RequestMapping(value = "/{id}",method = RequestMethod.DELETE)
    public Result delete(@PathVariable String id){
        spitService.delete(id);
        return new Result(true,StatusCode.OK,"删除成功");
    }

    /**
     * 根据上级ID查询吐槽数据
     */
    @RequestMapping(value = "/comment/{parentid}/{page}/{size}",method = RequestMethod.GET)
    public Result findByParentid(@PathVariable String parentid,@PathVariable int page,@PathVariable int size){
        Page<Spit> pageData = spitService.findByParentid(parentid,page,size);
        return new Result(true,StatusCode.OK,"查询成功",new PageResult<>(pageData.getTotalElements(),pageData.getContent()));
    }

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 吐槽点赞
     */
    @RequestMapping(value = "/thumbup/{id}",method = RequestMethod.PUT)
    public Result thumbup(@PathVariable String id){
        //模拟登录用户ID
        String userid = "1001";

        //1.查询redis看该用户（登录用户）是否点赞过
        String flag = (String)redisTemplate.opsForValue().get("thumbup_"+userid+"_"+id);


        if(flag==null){
            //1.1 没有，让用户点赞，把点赞行为记录到redis中
            spitService.thumbup(id);

            redisTemplate.opsForValue().set("thumbup_"+userid+"_"+id,"1");

            return new Result(true,StatusCode.OK,"点赞成功");
        }else{
            //1.2 有，提示用户不能重复点赞（或取消点赞）
            return new Result(false,StatusCode.REPEAT_ERROR,"你重复点赞啦");
        }
    }
}
