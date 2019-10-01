package com.xuecheng.manage_course.dao;

import com.xuecheng.framework.domain.course.CourseBase;
import org.springframework.data.jpa.repository.JpaRepository;


/**
 * Created by admin on 2018/2/7.
 */
public interface CourseBaseRepository extends JpaRepository<CourseBase, String> {
        CourseBase findCourseBaseByIdAndUserId(String id, String userId);
}
