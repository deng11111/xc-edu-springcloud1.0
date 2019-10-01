package com.xuecheng.manage_course;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.xuecheng.framework.domain.course.ext.CourseInfo;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.manage_course.dao.CourseMapper;
import com.xuecheng.manage_course.dao.TeachplahMapper;
import com.xuecheng.manage_course.service.CourseService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by mrt on 2018/4/11.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class MapperTest {


    @Autowired
    CourseMapper courseMapper;
    @Autowired
    TeachplahMapper teachplahMapper;

    @Autowired
    CourseService courseService;


    @Test
    public void testCourseMapper() {
        PageHelper.startPage(1, 2);
        Page<CourseInfo> courseListPage = courseMapper.findCourseListPage(null);
        System.out.println(courseListPage);
    }
    @Test
    public void testTeachplahMapper() {
        TeachplanNode teachplanNodes = teachplahMapper.selectList("402885816243d2dd016243f24c030002");
        System.out.println(teachplanNodes);
    }
}
