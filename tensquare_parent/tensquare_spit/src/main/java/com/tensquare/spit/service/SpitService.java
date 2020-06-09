package com.tensquare.spit.service;


import com.tensquare.spit.dao.SpitDao;
import com.tensquare.spit.pojo.Spit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import util.IdWorker;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 吐槽service
 */
@Service
public class SpitService {

    @Autowired
    private SpitDao spitDao;

    @Autowired
    private IdWorker idWorker;

    /**
     * 查询所有
     */
    public List<Spit> findAll(){
        return spitDao.findAll();
    }

    /**
     * 查询一个
     */
    public Spit findById(String id){
        return spitDao.findById(id).get();
    }

    /**
     * 添加（即是吐槽发布的方法，也是评论发布的方法）
     */
    public void add(Spit spit){
        //设置id
        spit.setId(idWorker.nextId()+"");
        spitDao.save(spit);

        //在用户发布评论的时候，让回复数+1
        //判断该数据是否为评论发布
        if(spit.getParentid()!=null && !spit.getParentid().equals("")){
            //让（评论对应的吐槽的）回复数+1

            Query query = new Query();
            query.addCriteria(Criteria.where("_id").is(spit.getParentid()));

            Update update = new Update();
            update.inc("comment",1);

            mongoTemplate.updateFirst(query,update,"spit");

        }
    }

    /**
     * 修改
     */
    public void update(Spit spit){// spit必须有数据库存在的id
        spitDao.save(spit);
    }

    /**
     * 删除
     */
    public void delete(String id){
        spitDao.deleteById(id);
    }

    /**
     * 根据上级ID查询吐槽
     */
    public Page<Spit> findByParentid(String parentid,int page,int size){
        return spitDao.findByParentid(parentid,PageRequest.of(page-1,size));
    }

    /**
     * 方案一：先查询点赞数，再+1，再写回集合中去
     */
    /*public void thumbup(String id){
        //1.先查询点赞数
        Spit spit = findById(id);
        //2.点赞数+1
        spit.setThumbup(spit.getThumbup()+1);
        //3.把数据写回到集合中
        update(spit);
    }*/

    /**
     * MongoTemplate：代表mongoDB的工具类，发送不同类似的命令
     */
    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * 方案二：直接使用$inc进行列增长
     */
    public void thumbup(String id){
        // db.spit.update( {"_id":"1081120626602725376"},{$inc:{ "thumbup":NumberInt(1)  }}  )

        //1.创建查询条件
        //{"_id":"1081120626602725376"}
        Query query = new Query();
        query.addCriteria(Criteria.where("_id").is(id));

        //2.创建更新对象
        //{$inc:{ "thumbup":NumberInt(1)  }
        Update update = new Update();
        update.inc("thumbup",1);

        //3.使用MongoTemplate执行update方法
        mongoTemplate.updateFirst(query,update,"spit");

        /**
         *   {"visits":{$gt:NumberInt(35)}}
         */
        /*Query query2 = new Query();
        query2.addCriteria(Criteria.where("visits").gt(35));*/




    }
}
