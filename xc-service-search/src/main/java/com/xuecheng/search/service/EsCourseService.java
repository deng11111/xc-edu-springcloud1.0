package com.xuecheng.search.service;

import com.xuecheng.framework.domain.course.CoursePub;
import com.xuecheng.framework.domain.course.ext.CourseInfo;
import com.xuecheng.framework.domain.search.CourseSearchParam;

import java.io.IOException;
import java.util.Map;

/**
 * Created by admin on 2018/2/18.
 */
public interface EsCourseService {

    //搜索
    public String list(int page,int size,CourseSearchParam courseSearchParam) throws IOException;

    //根据id查询课程基本信息
    public Map<String,CourseInfo> getbase(String ids) throws IOException;

    public Map<String,CoursePub> getall(String id);
}
