package com.xuecheng.manage_course.web.controller;

import com.xuecheng.api.course.CategoryControllerApi;
import com.xuecheng.framework.domain.course.ext.CategoryNode;
import com.xuecheng.manage_course.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by admin on 2018/2/7.
 */
@RestController
public class CategoryController implements CategoryControllerApi {

    @Autowired
    CategoryService categoryService;


    @Override
    public CategoryNode findList() {
        return categoryService.findList();
    }

}
