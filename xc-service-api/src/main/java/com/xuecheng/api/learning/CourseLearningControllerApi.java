package com.xuecheng.api.learning;

import com.xuecheng.api.learning.response.GetMediaResult;
import com.xuecheng.framework.domain.learning.request.QueryChooseCourseRequest;
import com.xuecheng.framework.domain.learning.response.ChooseCourseStatusResult;
import com.xuecheng.framework.model.response.QueryResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

/**
 * Created by admin on 2018/2/7.
 */


@Api(value = "录播课程学习管理",description = "录播课程学习管理")
public interface CourseLearningControllerApi {

    @GetMapping("/getmedia/{courseId}/{teachplanId}")
    @ApiOperation("获取课程学习地址")
    public GetMediaResult getmedia(@PathVariable String courseId,@PathVariable String teachplanId);
    @PostMapping("/choosecourse/list/{page}/{size}")
    @ApiOperation("选课列表")
    public QueryResponseResult list(@PathVariable int page,@PathVariable int size,@RequestBody QueryChooseCourseRequest queryChooseCourseRequest);

    @PostMapping("/learnstatus/{courseId}")
    @ApiOperation("课程学习状态")
    public ChooseCourseStatusResult learnstatus(@PathVariable String courseId);
}
