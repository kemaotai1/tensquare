package com.tensquare.friend.dao;

import com.tensquare.friend.pojo.NoFriend;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 非好友Dao
 */
public interface NoFriendDao extends JpaRepository<NoFriend,String>{
}
