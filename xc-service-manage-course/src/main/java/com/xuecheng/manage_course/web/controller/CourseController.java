package com.xuecheng.manage_course.web.controller;

import com.xuecheng.api.course.CourseControllerApi;
import com.xuecheng.framework.domain.cms.response.CoursePreviewResult;
import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.CourseMarket;
import com.xuecheng.framework.domain.course.CoursePic;
import com.xuecheng.framework.domain.course.Teachplan;
import com.xuecheng.framework.domain.course.ext.CourseInfo;
import com.xuecheng.framework.domain.course.ext.TeachplanExt;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.domain.course.request.CourseListRequest;
import com.xuecheng.framework.domain.course.response.AddCourseResult;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.framework.utils.XcOauth2Util;
import com.xuecheng.framework.web.BaseController;
import com.xuecheng.manage_course.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by mrt on 2018/4/12.
 */
@RestController
public class CourseController extends BaseController implements CourseControllerApi {
    @Autowired
    CourseService courseService;

    //课程预览
    @Override
    public CoursePreviewResult preview(@PathVariable("id") String id) {
        return courseService.preview(id);
    }

    @Override
    public CoursePreviewResult checkpreview(@PathVariable String id) {
        return null;
    }

    @Override
    public ResponseResult publish(@PathVariable String id) {
        return courseService.publish(id);
    }

    @Override
    public AddCourseResult addCourseBase(@RequestBody CourseBase courseBase) {
        //单位id先为空，待认证授权讲过再添加
        CourseBase courseBase1 = courseService.addCourseBase("",courseBase);
        if(courseBase1!=null){
            AddCourseResult addCourseResult = new AddCourseResult(CommonCode.SUCCESS, courseBase1.getId());
            return addCourseResult;
        }else{
            return new AddCourseResult(CommonCode.FAIL,null);
        }
    }

    @Override
    public ResponseResult addCoursePic(@RequestParam("courseId") String courseId, @RequestParam("pic") String pic) {
        CoursePic coursePic = courseService.saveCoursePic(courseId, pic);
        if(coursePic!=null){
            return new ResponseResult(CommonCode.SUCCESS);
        }
        return new ResponseResult(CommonCode.FAIL);
    }


    @Override
    public ResponseResult deleteCoursePic(@RequestParam("courseId") String courseId) {
        boolean b = courseService.deleteCoursePic(courseId);
        if(b){
            return new ResponseResult(CommonCode.SUCCESS);
        }
        return new ResponseResult(CommonCode.FAIL);
    }

//    @PreAuthorize("hasAuthority('course_find_pic')")
    @Override
    public CoursePic findCoursePicList(@PathVariable("courseId") String courseId) {
        return courseService.findCoursepicList(courseId);
    }

//    @PreAuthorize("hasAuthority('course_find_list')")
    @Override
    public QueryResult<CourseInfo> findCourseList(@PathVariable("page") int page,
                                                  @PathVariable("size") int size,
                                                  CourseListRequest courseListRequest) {
        //调用工具类取出用户信息
        XcOauth2Util xcOauth2Util = new XcOauth2Util();
        XcOauth2Util.UserJwt userJwt = xcOauth2Util.getUserJwtFromHeader(request);
        if(userJwt == null){
            ExceptionCast.cast(CommonCode.UNAUTHENTICATED);
        }
        String companyId = userJwt.getCompanyId();

        return courseService.findCourseList(companyId,page,size,courseListRequest);
    }

//    @PreAuthorize("hasAuthority('course_get_baseinfo')")
    @Override
    public CourseBase getCourseBaseById(@PathVariable("courseId") String courseId) throws RuntimeException {
        return courseService.getCoursebaseById(courseId);
    }

    @Override
    public AddCourseResult updateCourseBase(@PathVariable String id, @RequestBody CourseBase courseBase) {
        CourseBase courseBase1 = courseService.updateCoursebase(id, courseBase);
        if(courseBase1!=null){
            return new AddCourseResult(CommonCode.SUCCESS,courseBase1.getId());
        }else{
            return new AddCourseResult(CommonCode.FAIL,null);
        }
    }

    @Override
    public ResponseResult updateCourseMarket(@PathVariable("id") String id, @RequestBody CourseMarket courseMarket) {
        CourseMarket courseMarket_u = courseService.updateCourseMarket(id, courseMarket);
        if(courseMarket_u!=null){
            return new ResponseResult(CommonCode.SUCCESS);
        }else{
            return new ResponseResult(CommonCode.FAIL);
        }
    }

    @Override
    public CourseMarket getCourseMarketById(@PathVariable("courseId") String courseId) {
        return courseService.getCourseMarketById(courseId);
    }

    @Override
    public TeachplanNode findTeachplanList(@PathVariable("courseId") String courseId){
        return courseService.findTeachplanList(courseId);
    }

    @Override
    public Teachplan addTeachplan(@RequestBody Teachplan teachplan) {
        return courseService.addTeachplan(teachplan);
    }

    @Override
    public TeachplanExt getTeachplanById(@PathVariable("id") String id) {
        return courseService.findTeachplanExtById(id);
    }

    @Override
    public ResponseResult updateTeachplan(@PathVariable("id") String id, @RequestBody TeachplanExt teachplanExt) {
        Teachplan teachplan1 = courseService.updateTeachplan(id, teachplanExt);
        if(teachplan1!=null){
            return new ResponseResult(CommonCode.SUCCESS);
        }else{
            return new ResponseResult(CommonCode.FAIL);
        }
    }


    @Override
    public ResponseResult deleteTeachplan(@PathVariable("id") String id) {
        return null;
    }
}
