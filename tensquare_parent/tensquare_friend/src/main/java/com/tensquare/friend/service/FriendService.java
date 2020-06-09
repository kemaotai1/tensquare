package com.tensquare.friend.service;

import com.tensquare.friend.client.UserClient;
import com.tensquare.friend.dao.FriendDao;
import com.tensquare.friend.dao.NoFriendDao;
import com.tensquare.friend.pojo.Friend;
import com.tensquare.friend.pojo.NoFriend;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 交友service
 */
@Service
public class FriendService {

    @Autowired
    private FriendDao friendDao;

    @Autowired
    private NoFriendDao noFriendDao;

    @Autowired
    private UserClient userClient;

    /**
     * 添加好友
     */
    @Transactional
    public Integer addFriend(String userid,String friendid){
        //1.判断是否加过该好友
        if(friendDao.selectCount(userid,friendid)>0){
            //添加过该好友
            return 0;
        }

        //没有添加过

        //把好友添加到数据库
        Friend friend = new Friend();
        friend.setUserid(userid);
        friend.setFriendid(friendid);
        friend.setIslike("0");

        friendDao.save(friend);


        //判断对方是否添加过当前用户，如果添加过了，则把双方的好友记录的islike改为1
        if( friendDao.selectCount(friendid,userid)>0 ){
            friendDao.updateLike(userid,friendid,"1");
            friendDao.updateLike(friendid,userid,"1");
        }

        //远程调用用户微服务
        userClient.updateFollowcount(userid,1);//关注数+1
        userClient.updateFanscount(friendid,1);//粉丝数+1

        return 1;
    }

    /**
     * 添加非好友
     */
    public void addNoFriend(String userid,String friendid){
        NoFriend noFriend = new NoFriend();
        noFriend.setUserid(userid);
        noFriend.setFriendid(friendid);

        noFriendDao.save(noFriend);
    }

    /**
     * 删除好友
     */
    @Transactional
    public void deleteFriend(String userid,String friendid){
        //1.删除自己的好友记录
        friendDao.deleteFriend(userid,friendid);

        //2.判断对方是否添加过当前用户，如果加过的话，把对方的记录的islike改为0
        if( friendDao.selectCount(friendid,userid)>0  ){
            friendDao.updateLike(friendid,userid,"0");
        }

        //远程调用用户微服务
        userClient.updateFollowcount(userid,-1);//关注数+1
        userClient.updateFanscount(friendid,-1);//粉丝数+1
    }
}
