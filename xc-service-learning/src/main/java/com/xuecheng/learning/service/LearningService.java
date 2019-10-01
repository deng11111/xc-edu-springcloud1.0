package com.xuecheng.learning.service;

import com.xuecheng.api.learning.response.GetMediaResult;
import com.xuecheng.api.learning.response.LearningCode;
import com.xuecheng.api.portalview.PortalViewApiFacade;
import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.learning.XcLearningCourse;
import com.xuecheng.framework.domain.learning.response.XcLearningCourseEx;
import com.xuecheng.framework.domain.portalview.ViewCourseMedia;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.learning.client.CourseClient;
import com.xuecheng.learning.client.PortalViewClient;
import com.xuecheng.learning.dao.XcLearningCourseRepository;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by mrt on 2018/5/14.
 */
@Service
public class LearningService {
    private static final Logger LOGGER = LoggerFactory.getLogger(LearningService.class);

//    @Autowired
    PortalViewClient portalViewClient;
//    @Autowired
    CourseClient courseClient;
    @Autowired
    XcLearningCourseRepository xcLearningCourseRepository;

    @Autowired
    PortalViewApiFacade portalViewApiFacade;

    //获取课程学习地址
    public GetMediaResult getMedia(String courseId, String teachplanId) {
        //校验学生的学习权限...

        String mediaFileId = null;
        //远程调用视图服务获取课程媒资信息
        ViewCourseMedia viewCourseMedia = portalViewApiFacade.getViewCourseControllerApi().findMediaById(teachplanId);
        if (viewCourseMedia == null) {
            ExceptionCast.cast(LearningCode.LEARNING_GETMEDIA_ERROR);
        }
        //视频播放地址
        String mediaUrl = viewCourseMedia.getMediaUrl();
        if (StringUtils.isEmpty(mediaUrl)) {
            ExceptionCast.cast(LearningCode.LEARNING_GETMEDIA_NOTFOUND);
        }
        //响应结果对象
        GetMediaResult getMediaResult = new GetMediaResult(CommonCode.SUCCESS, mediaUrl);

        return getMediaResult;
    }

    //添加选课
    @Transactional
    public ResponseResult addcourse(String userId, String courseId,String valid,Date startTime,Date endTime) {
        if (StringUtils.isEmpty(courseId)) {
            ExceptionCast.cast(LearningCode.CHOOSECOURSE_COURSEISNULL);
        }
        XcLearningCourse xcLearningCourse = xcLearningCourseRepository.findXcLearningCourseByUserIdAndCourseId(userId, courseId);
        if (xcLearningCourse == null) {//没有选课记录则添加
            xcLearningCourse = new XcLearningCourse();
            xcLearningCourse.setUserId(userId);
            xcLearningCourse.setCourseId(courseId);
            xcLearningCourse.setValid(valid);
            xcLearningCourse.setStartTime(startTime);
            xcLearningCourse.setEndTime(endTime);
            xcLearningCourse.setStatus("501001");
            xcLearningCourseRepository.save(xcLearningCourse);
        } else {//有选课记录则更新日期
            xcLearningCourse.setValid(valid);
            xcLearningCourse.setStartTime(startTime);
            xcLearningCourse.setEndTime(endTime);
            xcLearningCourse.setStatus("501001");
            xcLearningCourseRepository.save(xcLearningCourse);
        }


        return new ResponseResult(CommonCode.SUCCESS);
    }
    //选课分页查询
    public QueryResult<XcLearningCourseEx> findListPage(XcLearningCourseEx xcLearningCourseEx,int page,int size){

        if(xcLearningCourseEx == null){
            xcLearningCourseEx = new XcLearningCourseEx();
        }
        //页码
        if(page<1){
            page = 1;
        }
        if(size<0){
            size = 20;
        }
        //排序字段
        String sortField = xcLearningCourseEx.getSortField();
        if(StringUtils.isEmpty(sortField)){
            sortField = "startTime";
        }
        //排序类型
        Sort.Direction direction = null;
        if(StringUtils.isNotBlank(xcLearningCourseEx.getSortType())){
            String sortType = xcLearningCourseEx.getSortType();
            if(sortType.equals("desc")){
                direction = Sort.Direction.DESC;
            }else{
                direction = Sort.Direction.ASC;
            }
        }

        //排序对象
        Sort sort = new Sort(direction, sortField);

        //分页对象
        Pageable pageable = new PageRequest(page-1,size,sort);

        //条件规则定义
        final XcLearningCourseEx finalXcLearningCourseEx = xcLearningCourseEx;
        Specification<XcLearningCourse> specification = new Specification<XcLearningCourse>() {
            /**
             * 创建断言
             * @param root 实体对象
             * @param query 查询对象
             * @param cb 条件构建对象
             * @return 断言实例
             */
            @Override
            public Predicate toPredicate(Root<XcLearningCourse> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                //添加条件
                if(StringUtils.isNotBlank(finalXcLearningCourseEx.getUserId())){
                    Predicate userId = cb.equal(root.get("userId").as(String.class), finalXcLearningCourseEx.getUserId());
                    predicates.add(userId);
                }

                return cb.and(predicates.toArray(new Predicate[0]));
            }
        };

        Page<XcLearningCourse> list = xcLearningCourseRepository.findAll(specification, pageable);
        long total = list.getTotalElements();
        List<XcLearningCourse> content = list.getContent();
        List<XcLearningCourseEx> xcLearningCourseExes = new ArrayList<XcLearningCourseEx>();

        QueryResult queryResult = new QueryResult();
        queryResult.setList(content);
        queryResult.setTotal(total);
        return queryResult;
    }

    //获取选课状态
    public XcLearningCourse findChooseCourseByCourseId(String userId, String courseId) {
        final CourseBase courseBaseById = courseClient.getCourseBaseById(courseId);
        XcLearningCourse xcLearningCourse = xcLearningCourseRepository.findXcLearningCourseByUserIdAndCourseId(userId, courseId);
        return xcLearningCourse;

    }
}
