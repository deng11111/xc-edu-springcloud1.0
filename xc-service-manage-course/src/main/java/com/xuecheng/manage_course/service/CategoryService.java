package com.xuecheng.manage_course.service;

import com.xuecheng.framework.domain.course.ext.CategoryNode;
import com.xuecheng.manage_course.dao.CategoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by admin on 2018/2/7.
 */
@Service
public class CategoryService {


    @Autowired
    CategoryMapper categoryMapper;

    /**
     * 查询课程分类
     *
     * @return
     */
    public CategoryNode findList() {
        CategoryNode categoryNodes = categoryMapper.selectList();
        return categoryNodes;
    }

}
