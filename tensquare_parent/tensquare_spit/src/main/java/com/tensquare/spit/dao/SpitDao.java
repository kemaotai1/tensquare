package com.tensquare.spit.dao;

import com.tensquare.spit.pojo.Spit;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * 吐槽dao
 *  MongoRepository接口；拥有CRUD方法
 */
public interface SpitDao extends MongoRepository<Spit,String>{

    public Page<Spit> findByParentid(String parentid, Pageable pageable);
}
