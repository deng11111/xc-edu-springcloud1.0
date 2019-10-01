package com.xuecheng.manage_course.dao;

import com.xuecheng.framework.domain.course.CoursePic;
import org.springframework.data.jpa.repository.JpaRepository;


/**
 * Created by admin on 2018/2/7.
 */
public interface CoursePicRepository extends JpaRepository<CoursePic, String> {
    Long  deleteByCourseid(String courseid);

}
