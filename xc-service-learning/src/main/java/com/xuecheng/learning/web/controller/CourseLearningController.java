package com.xuecheng.learning.web.controller;

import com.xuecheng.api.learning.CourseLearningControllerApi;
import com.xuecheng.api.learning.response.GetMediaResult;
import com.xuecheng.framework.client.XcServiceList;
import com.xuecheng.framework.domain.learning.XcLearningCourse;
import com.xuecheng.framework.domain.learning.request.QueryChooseCourseRequest;
import com.xuecheng.framework.domain.learning.response.ChooseCourseStatusResult;
import com.xuecheng.framework.domain.learning.response.XcLearningCourseEx;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.framework.utils.XcOauth2Util;
import com.xuecheng.framework.web.BaseController;
import com.xuecheng.learning.service.LearningService;
import org.apache.servicecomb.provider.rest.common.RestSchema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * Created by mrt on 2018/5/14.
 */
@RequestMapping("/learning")
@RestSchema(schemaId = XcServiceList.XC_SERVICE_LEARNING)
//@RestController
public class CourseLearningController extends BaseController implements CourseLearningControllerApi {

    @Autowired
    LearningService learningService;

    @GetMapping("/getmedia/{courseId}/{teachplanId}")
    @Override
    public GetMediaResult getmedia(@PathVariable String courseId, @PathVariable String teachplanId) {
        //获取课程学习地址
        GetMediaResult media = learningService.getMedia(courseId, teachplanId);
        return media;
    }

    @PostMapping("/choosecourse/list/{page}/{size}")
    @Override
    public QueryResponseResult list(@PathVariable int page, @PathVariable int size, @RequestBody QueryChooseCourseRequest queryChooseCourseRequest) {
        //调用工具类取出用户信息
        XcOauth2Util xcOauth2Util = new XcOauth2Util();
        XcOauth2Util.UserJwt userJwt = xcOauth2Util.getUserJwtFromHeader(request);
        if(userJwt == null){
            ExceptionCast.cast(CommonCode.UNAUTHENTICATED);
        }
        String userId = userJwt.getId();
        //查询 条件
        XcLearningCourseEx xcLearningCourseExQuery = new XcLearningCourseEx();
        xcLearningCourseExQuery.setUserId(userId);
        xcLearningCourseExQuery.setSortField(xcLearningCourseExQuery.getSortField());
        xcLearningCourseExQuery.setSortType(xcLearningCourseExQuery.getSortType());
        QueryResult<XcLearningCourseEx> listPage = learningService.findListPage(xcLearningCourseExQuery, page, size);
        return new QueryResponseResult(CommonCode.SUCCESS,listPage);
    }

    @PostMapping("/learnstatus/{courseId}")
    @Override
    public ChooseCourseStatusResult learnstatus(@PathVariable String courseId) {
        //调用工具类取出用户信息
        XcOauth2Util xcOauth2Util = new XcOauth2Util();
        XcOauth2Util.UserJwt userJwt = xcOauth2Util.getUserJwtFromHeader(request);
        if(userJwt == null){
            ExceptionCast.cast(CommonCode.UNAUTHENTICATED);
        }
        String userId = userJwt.getId();
        XcLearningCourse chooseCourseByCourseId = learningService.findChooseCourseByCourseId(userId, courseId);
        if(chooseCourseByCourseId!=null){
            return new ChooseCourseStatusResult(CommonCode.SUCCESS,chooseCourseByCourseId.getStatus());
        }
        return new ChooseCourseStatusResult(CommonCode.SUCCESS,"501004");//未选课
    }
}
