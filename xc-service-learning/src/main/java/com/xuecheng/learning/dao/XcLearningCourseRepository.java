package com.xuecheng.learning.dao;

import com.xuecheng.framework.domain.learning.XcLearningCourse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by mrt on 2018/4/2.
 */
public interface XcLearningCourseRepository extends JpaRepository<XcLearningCourse, String> {
    //根据用户和课程查询选课记录，用于判断是否添加选课
    XcLearningCourse findXcLearningCourseByUserIdAndCourseId(String userId, String courseId);
    Page<XcLearningCourse> findAll(Specification<XcLearningCourse> spec, Pageable pageable);
}
