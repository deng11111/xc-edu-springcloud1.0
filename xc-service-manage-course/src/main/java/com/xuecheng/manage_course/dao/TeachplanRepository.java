package com.xuecheng.manage_course.dao;

import com.xuecheng.framework.domain.course.Teachplan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


/**
 * Created by admin on 2018/2/7.
 */
public interface TeachplanRepository extends JpaRepository<Teachplan, String> {
    List<Teachplan> findByParentid(String parentid);

    Long deleteByCourseid(String courseid);
    Long deleteByCourseidAndId(String courseid, String id);
}
