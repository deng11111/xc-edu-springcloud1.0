package com.xuecheng.search.web.controller;

import com.alibaba.fastjson.JSON;
import com.xuecheng.api.search.EsCourseControllerApi;
import com.xuecheng.framework.client.XcServiceList;
import com.xuecheng.framework.domain.course.CoursePub;
import com.xuecheng.framework.domain.course.ext.CourseInfo;
import com.xuecheng.framework.domain.search.CourseSearchParam;
import com.xuecheng.search.service.EsCourseService;
import org.apache.servicecomb.provider.rest.common.RestSchema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.Map;


//@RestController
@RequestMapping("/search/course")
@RestSchema(schemaId = XcServiceList.XC_SERVICE_SEARCH)
public class EsCourseController implements EsCourseControllerApi {

    @Autowired
    EsCourseService esCourseService;

    @GetMapping(API_PRE+"/list/{page}/{size}")
    @Override
    public  Map<String,Object> list(@PathVariable("page") int page,
                     @PathVariable("size") int size,
                     @RequestParam("keyword") String keyword,
                     @RequestParam("mt") String mt,
                     @RequestParam("st") String st,
                     @RequestParam("grade") String grade) throws IOException {
        CourseSearchParam courseSearchParam = new CourseSearchParam();
        courseSearchParam.setKeyword(keyword);
        courseSearchParam.setMt(mt);
        courseSearchParam.setSt(st);
        courseSearchParam.setGrade(grade);
        String result = esCourseService.list(page,size,courseSearchParam);
        Map test = null;
        try {
            test = JSON.parseObject(result, Map.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return test;
    }

    @GetMapping(value=API_PRE+"/getbase/{ids}")
    @Override
    public Map<String,CourseInfo> getbase(@PathVariable("ids") String ids) throws IOException {
        return esCourseService.getbase(ids);
    }

    @GetMapping(value=API_PRE+"/getall/{id}")
    @Override
    public Map<String, CoursePub> getall(@PathVariable("id") String id) throws IOException {
        return esCourseService.getall(id);
    }
}
