package com.tensquare.search.service;

import com.tensquare.search.dao.ArticleDao;
import com.tensquare.search.pojo.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

/**
 * 文章service
 */
@Service
public class ArticleService {

    @Autowired
    private ArticleDao articleDao;

    /**
     * 搜索
     */
    public Page<Article> search(String keyword, int page, int size){
        /**
         * 使用命名查询
         */
        //return articleDao.findByTitleLikeOrContentLike(keyword,keyword, PageRequest.of(page-1,size));
        return articleDao.findByTitleOrContentLike(keyword,keyword, PageRequest.of(page-1,size));
    }

}
