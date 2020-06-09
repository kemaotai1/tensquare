package com.tensquare.friend.controller;

import com.tensquare.friend.service.FriendService;
import entity.Result;
import entity.StatusCode;
import io.jsonwebtoken.Claims;
import org.apache.catalina.servlet4preview.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 交友Controller
 */
@RestController
@RequestMapping("/friend")
@CrossOrigin
public class FriendController {

    @Autowired
    private FriendService friendService;

    @Autowired
    private HttpServletRequest request;

    /**
     * 添加好友或非好友
     */
    @RequestMapping(value = "/like/{friendid}/{type}",method = RequestMethod.PUT)
    public Result addFriend(@PathVariable String friendid, @PathVariable String type){
        //1.获取当前登录用户ID
        Claims claims = (Claims)request.getAttribute("user_claims");
        if(claims==null){
            return new Result(false, StatusCode.ACCESS_ERROR,"请先登录");
        }
        String userid = claims.getId();

        if(type.equals("1")){
            //添加好友
            Integer flag = friendService.addFriend(userid,friendid);
            if(flag==0){
                //重复添加好友
                return new Result(false,StatusCode.REPEAT_ERROR,"重复添加好友啦");
            }else{
                return new Result(true,StatusCode.OK,"添加好友成功");
            }

        }else{
            //添加非好友
            friendService.addNoFriend(userid,friendid);

            return new Result(true,StatusCode.OK,"添加非好友成功");
        }

    }

    /**
     * 删除好友
     */
    @RequestMapping(value = "/{friendid}",method = RequestMethod.DELETE)
    public Result deleteFriend(@PathVariable String friendid){
        //1.获取当前登录用户ID
        Claims claims = (Claims)request.getAttribute("user_claims");
        if(claims==null){
            return new Result(false, StatusCode.ACCESS_ERROR,"请先登录");
        }
        String userid = claims.getId();

        //2.删除好友
        friendService.deleteFriend(userid,friendid);
        return new Result(true,StatusCode.OK,"删除好友成功");
    }
}
