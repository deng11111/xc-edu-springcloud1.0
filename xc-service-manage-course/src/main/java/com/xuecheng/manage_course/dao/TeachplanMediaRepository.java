package com.xuecheng.manage_course.dao;

import com.xuecheng.framework.domain.course.TeachplanMedia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


/**
 * Created by admin on 2018/2/7.
 */
public interface TeachplanMediaRepository extends JpaRepository<TeachplanMedia, String> {
    //根据课程id查询课程媒资
    List<TeachplanMedia> findByCourseId(String courseid);
}
