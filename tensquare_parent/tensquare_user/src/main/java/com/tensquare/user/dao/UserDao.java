package com.tensquare.user.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.tensquare.user.pojo.User;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

/**
 * 数据访问接口
 * @author Administrator
 *
 */
public interface UserDao extends JpaRepository<User,String>,JpaSpecificationExecutor<User>{

    /**
     * 查询账户是否存在
     */
    public User findByMobile(String mobile);

    /**
     * 更新关注数
     */
    @Modifying
    @Query("update User u set u.followcount = u.followcount + ?2 where u.id = ?1")
    public void updateFollowcount(String userid,int x);

    /**
     * 更新粉丝数
     */
    @Modifying
    @Query("update User u set u.fanscount = u.fanscount + ?2 where u.id = ?1")
    public void updateFanscount(String userid,int x);
}
