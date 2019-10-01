package com.xuecheng.manage_course.dao;

import com.xuecheng.framework.domain.course.ext.TeachplanNode;


/**
 * Created by admin on 2018/2/7.
 */
public interface TeachplahMapper {
    public TeachplanNode selectList(String courseId);
}
