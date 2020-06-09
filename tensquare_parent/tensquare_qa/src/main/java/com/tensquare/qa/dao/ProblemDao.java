package com.tensquare.qa.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.tensquare.qa.pojo.Problem;
import org.springframework.data.jpa.repository.Query;

/**
 * 数据访问接口
 * @author Administrator
 *
 */
public interface ProblemDao extends JpaRepository<Problem,String>,JpaSpecificationExecutor<Problem>{

    /**
     * 最新问答
     *  SQL语句：SELECT * FROM tb_problem p WHERE p.id
     *       IN (SELECT pl.problemid FROM tb_pl pl WHERE pl.labelid = '1080406824395534336');
     *
     *  翻译SQL语句成JPQL：
     *        select p from Problem p where p.id
     *           in ( select pl.problemid from  Pl pl where pl.labelid = ?1)
     */
    @Query("select p from Problem p where p.id" +
            "   in ( select pl.problemid from  Pl pl where pl.labelid = ?1) order by p.replytime desc")
    public Page<Problem> newlist(String labelid, Pageable pageable);

    /**
     * 热门问答
     */
    @Query("select p from Problem p where p.id" +
            "   in ( select pl.problemid from  Pl pl where pl.labelid = ?1) order by p.reply desc")
    public Page<Problem> hotlist(String labelid, Pageable pageable);

    /**
     * 等待问答
     */
    @Query("select p from Problem p where p.id" +
            "   in ( select pl.problemid from  Pl pl where pl.labelid = ?1) and p.reply = 0 ")
    public Page<Problem> waitlist(String labelid, Pageable pageable);
}
