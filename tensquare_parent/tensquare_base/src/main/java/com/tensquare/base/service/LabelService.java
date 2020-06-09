package com.tensquare.base.service;

import com.tensquare.base.dao.LabelDao;
import com.tensquare.base.pojo.Label;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import util.IdWorker;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Map;
import java.util.List;

/**
 * 标签service
 */
@Service
public class LabelService {

    @Autowired
    private LabelDao labelDao;

    @Autowired
    private IdWorker idWorker;

    /**
     * 查询所有
     */
    public List<Label> findAll(){
        return labelDao.findAll();
    }

    /**
     * 查询一个
     */
    public Label findById(String id){
        return labelDao.findById(id).get();
    }

    /**
     * 添加
     */
    public void add(Label label){
        //设置id
        label.setId(idWorker.nextId()+"");
        labelDao.save(label);
    }

    /**
     * 修改
     */
    public void update(Label label){// label必须有数据库存在的id
        labelDao.save(label);
    }

    /**
     * 删除
     */
    public void delete(String id){
        labelDao.deleteById(id);
    }

    /**
     * 创建Specification对象
     */
    private Specification<Label> createSpecification(Map searchMap){
        //自行提供Specification接口的实现类对象(匿名内部类写法)
        return new Specification<Label>() {
            @Override
            public Predicate toPredicate(Root<Label> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder cb) {
               //1.准备一个List集合，用于存放条件对象
               List<Predicate> preList = new ArrayList<Predicate>();

               //2.组装条件对象，放入List集合中
                // 根据标签名称搜索
                if( searchMap.get("labelname")!=null && !searchMap.get("labelname").equals("")){
                    // labelname like '%xxx%'
                    preList.add( cb.like( root.get("labelname").as(String.class), "%"+searchMap.get("labelname")+"%"  ) );
                }

                if( searchMap.get("state")!=null && !searchMap.get("state").equals("")){
                    // state = '1'
                    preList.add( cb.equal( root.get("state").as(String.class), searchMap.get("state") ) );
                }

                if( searchMap.get("recommend")!=null && !searchMap.get("recommend").equals("")){
                    // recommend = '1'
                    preList.add( cb.equal( root.get("recommend").as(String.class), searchMap.get("recommend") ) );
                }

                //3. 使用连接条件把（and或者or）List的所有条件进行连接
                // where labelname like '%xxx%' and  state = '1' and recommend = '1'
                /**
                 * preList.toArray(): 把preList集合中每个元素取出，逐一放入新的Object数组中，返回Object数组
                 * preList.toArray(preArray): 把preList集合总每个元素取出，逐一放入指定的preArray数组，返回preArray数组
                 */
                Predicate[] preArray = new Predicate[preList.size()];
                return cb.and(preList.toArray(preArray));
            }
        };
    }


    /**
     * 条件查询
     */
    public List<Label> findSearch(Map searchMap){
        Specification<Label> spec = createSpecification(searchMap);
        return labelDao.findAll(spec);
    }

    /**
     * 带条件的分页查询
     */
    public Page<Label> findSearch(Map searchMap, int page, int size){
        Specification<Label> spec = createSpecification(searchMap);
        //Pageable: 封装分页参数（当前页码，页面大小），提供Pageable实现类对象：PageRequest
        //注意： Pageable接口里面的page从0开始的
        return labelDao.findAll(spec, PageRequest.of(page-1,size));
    }
}
