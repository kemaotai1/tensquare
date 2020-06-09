package com.tensquare.friend.dao;

import com.tensquare.friend.pojo.Friend;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * 好友dao接口
 */
public interface FriendDao extends JpaRepository<Friend,String>{

    /***
     * 查询好友记录数
     */
    @Query("select count(f) from Friend f where f.userid = ?1 and f.friendid = ?2")
    public Long selectCount(String userid,String friendid);

    /**
     * 更新islike值
     */
    @Modifying
    @Query("update Friend f set f.islike = ?3 where f.userid = ?1 and f.friendid = ?2")
    public void updateLike(String userid,String friendid,String islike);

    /**
     * 删除好友记录
     */
    @Modifying
    @Query("delete from Friend f where f.userid = ?1 and f.friendid = ?2")
    public void deleteFriend(String userid,String friendid);
}
