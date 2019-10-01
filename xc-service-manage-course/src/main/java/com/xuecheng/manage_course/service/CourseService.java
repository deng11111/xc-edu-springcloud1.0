package com.xuecheng.manage_course.service;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.xuecheng.framework.domain.cms.response.CoursePreviewResult;
import com.xuecheng.framework.domain.course.*;
import com.xuecheng.framework.domain.course.ext.CourseInfo;
import com.xuecheng.framework.domain.course.ext.TeachplanExt;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.domain.course.request.CourseListRequest;
import com.xuecheng.framework.domain.course.response.CourseCode;
import com.xuecheng.framework.domain.portalview.PreViewCourse;
import com.xuecheng.framework.domain.portalview.PreViewCourseMedia;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_course.client.CmsCourseClient;
import com.xuecheng.manage_course.client.PortalViewClient;
import com.xuecheng.manage_course.dao.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by admin on 2018/2/7.
 */
@Service
public class CourseService {

    @Autowired
    CourseBaseRepository courseBaseRepository;
    @Autowired
    CourseMapper courseMapper;
    @Autowired
    CoursePicRepository coursePicRepository;
    @Autowired
    CourseMarketRepository courseMarketRepository;
    @Autowired
    TeachplahMapper teachplahMapper;
    @Autowired
    TeachplanRepository teachplanRepository;
    @Autowired
    TeachplanMediaRepository teachplanMediaRepository;
    @Autowired
    CoursePubRepository coursePubRepository;
    @Autowired
    PortalViewClient portalViewClient;
    @Autowired
    CmsCourseClient cmsCourseClient;
    //添加课程基础信息
    @Transactional
    public CourseBase addCourseBase(String companyId,CourseBase courseBase) {
        //课程状态默认为未发布
        courseBase.setStatus("202001");
        courseBase.setCompanyId(companyId);
        CourseBase save = courseBaseRepository.save(courseBase);
        return save;
    }


    //删除课程图片
    @Transactional
    public boolean deleteCoursePic(String courseid) {
        Long aLong = coursePicRepository.deleteByCourseid(courseid);
        if(aLong>0){
            return true;
        }else{
            return false;
        }
    }


    //保存课程图片
    @Transactional
    public CoursePic saveCoursePic(String courseId, String pic) {
        CoursePic one = coursePicRepository.findOne(courseId);
        if(one!=null){
            one.setPic(pic);
            CoursePic save = coursePicRepository.save(one);
            return save;
        }else{
            CoursePic coursePic = new CoursePic();
            coursePic.setCourseid(courseId);
            coursePic.setPic(pic);
            CoursePic save = coursePicRepository.save(coursePic);
            return save;
        }

    }


    public CoursePic findCoursepicList(String courseId) {
        return coursePicRepository.findOne(courseId);
    }

    public QueryResult<CourseInfo> findCourseList(String companyId,int page,int size,CourseListRequest courseListRequest) {
        if(courseListRequest == null){
            courseListRequest = new CourseListRequest();
        }
        //判断公司 id，如果没有值则强调让用户登录
        if(StringUtils.isEmpty(companyId)){
            ExceptionCast.cast(CommonCode.UNAUTHENTICATED);
        }
        //将companyId传给dao
        courseListRequest.setCompanyId(companyId);
        if(page<=0){
            page = 0;
        }
        if(size<=0){
            size = 20;
        }
        PageHelper.startPage(page, size);
        Page<CourseInfo> courseListPage = courseMapper.findCourseListPage(courseListRequest);
        List<CourseInfo> list = courseListPage.getResult();
        long total = courseListPage.getTotal();
        QueryResult<CourseInfo> courseIncfoQueryResult = new QueryResult<CourseInfo>();
        courseIncfoQueryResult.setList(list);
        courseIncfoQueryResult.setTotal(total);
        return courseIncfoQueryResult;
    }

    public CourseBase getCoursebaseById(String courseid) {
        CourseBase one = courseBaseRepository.findOne(courseid);
        return one;
    }

    @Transactional
    public CourseBase updateCoursebase(String id, CourseBase courseBase) {
        CourseBase one = courseBaseRepository.findOne(id);
        one.setName(courseBase.getName());
        one.setMt(courseBase.getMt());
        one.setSt(courseBase.getSt());
        one.setGrade(courseBase.getGrade());
        one.setStudymodel(courseBase.getStudymodel());
        one.setUsers(courseBase.getUsers());
        one.setDescription(courseBase.getDescription());
        CourseBase save = courseBaseRepository.save(one);
        return save;
    }

    @Transactional
    public CourseMarket updateCourseMarket(String id, CourseMarket courseMarket) {
        CourseMarket one = courseMarketRepository.findOne(id);
        if(one!=null){
            one.setCharge(courseMarket.getCharge());
            one.setStartTime(courseMarket.getStartTime());//课程有效期，开始时间
            one.setEndTime(courseMarket.getEndTime());//课程有效期，结束时间
//            one.setExpires(courseMarket.getExpires());
            one.setPrice(courseMarket.getPrice());
            one.setQq(courseMarket.getQq());
            one.setValid(courseMarket.getValid());
            courseMarketRepository.save(one);
        }else{
            //添加课程营销信息
            one = new CourseMarket();
            BeanUtils.copyProperties(courseMarket, one);
            //设置课程id
            one.setId(id);
            courseMarketRepository.save(one);
        }
        return one;
    }

    public CourseMarket getCourseMarketById(String courseid) {
        return courseMarketRepository.findOne(courseid);
    }


    //添加根结点
    @Transactional
    public Teachplan addTeachplanRoot(String rootId,String rootName) {
        Teachplan teachplan = new Teachplan();
        teachplan.setCourseid(rootId);
        teachplan.setPname(rootName);
        teachplan.setParentid("0");
        teachplan.setStatus("0");
        teachplan.setOrderby(1);
        teachplan.setGrade("1");//一级分类
        Teachplan save = teachplanRepository.save(teachplan);
        return save;
    }

    //查询课程计划
    public TeachplanNode findTeachplanList(String courseId){
        TeachplanNode teachplanNode = teachplahMapper.selectList(courseId);
        return teachplanNode;
    }

    @Transactional
    public Teachplan addTeachplan(Teachplan teachplan) {
        //courseid,pname,parentid必填
        //获取parentid
        String parentid = teachplan.getParentid();
        //根据parentid查询结点信息，目的是确定 新结点的grade、orderby等字段
        Teachplan parentNode = teachplanRepository.findOne(parentid);
        //一些添加结点的规则校验，根据parentNode校验
        //.....
        //获取grade
        String parentNode_grade = parentNode.getGrade();
        String newNode_grade = "";
        //确定 grade
        if(parentNode_grade.equals("1")){
            newNode_grade = "2";
        }else if(parentNode_grade.equals("2")){
            newNode_grade = "3";
        }
        teachplan.setGrade(newNode_grade);
        //确定 orderby
        int orderby_new = 0;
        //取出parentNode的子结点
        List<Teachplan> childrenNodes = teachplanRepository.findByParentid(parentNode.getId());
        if(childrenNodes!=null){

            orderby_new = childrenNodes.size()+1;
            teachplan.setOrderby(orderby_new);
        }
        //设置父结点id
        teachplan.setParentid(parentNode.getId());
        //设置初始状态
        teachplan.setStatus("0");

        return teachplanRepository.save(teachplan);
    }

    //查询课程计划
    public Teachplan findTeachplanById(String id) {
        return teachplanRepository.findOne(id);
    }
    //查询课程媒资文件
    public TeachplanMedia findTeachplanMediaById(String id) {
        return teachplanMediaRepository.findOne(id);
    }
    //查询课程计划及媒资文件
    public TeachplanExt findTeachplanExtById(String id) {
        Teachplan teachplan = teachplanRepository.findOne(id);
        TeachplanMedia teachplanMedia = teachplanMediaRepository.findOne(id);

        TeachplanExt teachplanExt = new TeachplanExt();
        BeanUtils.copyProperties(teachplan,teachplanExt);
        if(teachplanMedia!=null){
            BeanUtils.copyProperties(teachplanMedia,teachplanExt);
        }

        return teachplanExt;
    }
    //更新课程计划
    @Transactional
    public Teachplan updateTeachplan(String id,TeachplanExt teachplanExt) {
        Teachplan teachplan = teachplanRepository.getOne(id);
        teachplan.setPname(teachplanExt.getPname());
        teachplan.setStatus(teachplanExt.getStatus());
        teachplan.setDescription(teachplanExt.getDescription());
        teachplan.setPtype(teachplanExt.getPtype());
        teachplan.setOrderby(teachplanExt.getOrderby());
        teachplan.setTimelength(teachplanExt.getTimelength());
        Teachplan save = teachplanRepository.save(teachplan);
        //当选择了媒资文件时进行判断，媒资文件id和媒资文件原始名称和访问url不能为空
        if(StringUtils.isNotEmpty(teachplanExt.getMediaId())){
            if(StringUtils.isEmpty(teachplanExt.getMediaUrl())){
                ExceptionCast.cast(CourseCode.COURSE_MEDIS_URLISNULL);
            }
            if(StringUtils.isEmpty(teachplanExt.getMediaFileOriginalName())){
                ExceptionCast.cast(CourseCode.COURSE_MEDIS_NAMEISNULL);
            }
        }
        //保存课程计划媒资文件信息，有则更新，没有则添加
        if(StringUtils.isNotEmpty(teachplanExt.getMediaId())){
            String teachplayId = teachplan.getId();
            TeachplanMedia teachplanMedia = teachplanMediaRepository.findOne(teachplayId);
            if(teachplanMedia == null){
                teachplanMedia = new TeachplanMedia();
            }
            teachplanMedia.setTeachplanId(teachplayId);
            //课程id
            teachplanMedia.setCourseId(teachplan.getCourseid());
            //媒资文件id
            teachplanMedia.setMediaId(teachplanExt.getMediaId());
            //媒资文件原始名称
            teachplanMedia.setMediaFileOriginalName(teachplanExt.getMediaFileOriginalName());
            //媒资文件访问地址
            teachplanMedia.setMediaUrl(teachplanExt.getMediaUrl());
            teachplanMediaRepository.save(teachplanMedia);
        }

        return save;
    }

    //课程预览
    public CoursePreviewResult preview(String courseId){
        //保存数据视图
        ResponseResult responseResult = this.saveViewCourse(courseId);
        if(responseResult == null || !responseResult.isSuccess()){
            ExceptionCast.cast(CommonCode.FAIL);
        }
        //创建课程预览页面
        CoursePreviewResult cpreview = cmsCourseClient.cpreview(courseId);
        if(cpreview == null || !cpreview.isSuccess()){
            ExceptionCast.cast(CommonCode.FAIL);
        }
        return cpreview;

    }
    //存储课程视图信息--临时部分用静态数据表示
    public ResponseResult saveViewCourse(String courseId){
        PreViewCourse viewCourse = new PreViewCourse();
        viewCourse.setId(courseId);
        CourseBase courseBase = courseBaseRepository.findOne(courseId);
        CoursePic coursePic = coursePicRepository.findOne(courseId);
        CourseMarket courseMarket = courseMarketRepository.findOne(courseId);
        viewCourse.setCourseBase(courseBase);
        viewCourse.setCoursePic(coursePic);
        viewCourse.setCourseMarket(courseMarket);
        //查询课程计划
        TeachplanNode teachplanNodes = teachplahMapper.selectList(courseId);
        viewCourse.setTeachplan(teachplanNodes);

        ResponseResult responseResult = portalViewClient.add(courseId,viewCourse);
        //查询课程媒资
        List<TeachplanMedia> teachplanMediaList = teachplanMediaRepository.findByCourseId(courseId);
        List<PreViewCourseMedia> preViewCourseMedias = new ArrayList<>();
        for(TeachplanMedia teachplanMedia:teachplanMediaList){
            PreViewCourseMedia preViewCourseMedia = new PreViewCourseMedia();
            BeanUtils.copyProperties(teachplanMedia,preViewCourseMedia);
            preViewCourseMedias.add(preViewCourseMedia);
        }
        //调用portalview存储课程媒资
        ResponseResult addmediaResult = portalViewClient.addmedia(courseId, preViewCourseMedias);

        return addmediaResult;
    }
    //发布课程
    @Transactional
    public ResponseResult publish(String id) {
        //校验是否生成预览页面
        //...

        //创建课程索引信息
        CoursePub coursepub = createCoursepub(id);
        saveCoursePub(id,coursepub);
        //更新课程发布状态
        saveCoursePubState(id);

        //调用 cms创建课程详情页面
        ResponseResult responseResult = cmsCourseClient.cdetail(id);
        if(responseResult == null || !responseResult.isSuccess()){
            ExceptionCast.cast(CourseCode.COURSE_PUBLISH_CDETAILERROR);
        }
        //发布课程视图
        ResponseResult publishResult = portalViewClient.publish(id);
        if(publishResult == null || !publishResult.isSuccess()){
            ExceptionCast.cast(CourseCode.COURSE_PUBLISH_VIEWERROR);
        }
        return responseResult;
    }
    //保存课程发布信息
    private CoursePub saveCoursePub(String courseId,CoursePub coursePub){
        if(StringUtils.isEmpty(courseId)){
            ExceptionCast.cast(CourseCode.COURSE_PUBLISH_COURSEIDISNULL);
        }
        CoursePub one = coursePubRepository.findOne(courseId);
        //更新课程发布信息
        if(one!=null){
            String pubTime = one.getPubTime();
            BeanUtils.copyProperties(coursePub,one);
            //更新时间戳为最新时间
            one.setTimestamp(new Date());
            //发布时间
            one.setPubTime(pubTime);
            //设置id
            one.setId(courseId);
            CoursePub save = coursePubRepository.save(one);
            return save;
        }else{//添加课程发布信息
            //更新时间戳为最新时间
            coursePub.setTimestamp(new Date());
            //发布时间
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("YYYY-MM-dd HH:mm:ss");
            String date = simpleDateFormat.format(new Date());
            coursePub.setPubTime(date);
            //设置id
            coursePub.setId(courseId);
            CoursePub save = coursePubRepository.save(coursePub);
            return save;
        }

    }
    //创建课程发布信息，作为课程的索引信息
    private CoursePub createCoursepub(String id){
        CoursePub coursePub = new CoursePub();
        coursePub.setId(id);

        //基础信息
        CourseBase courseBase = courseBaseRepository.findOne(id);
        BeanUtils.copyProperties(courseBase,coursePub);

        //营销信息
        CourseMarket courseMarket = courseMarketRepository.findOne(id);
        BeanUtils.copyProperties(courseMarket,coursePub);

        //图片信息
        CoursePic one = coursePicRepository.findOne(id);
        if(one!=null){
            coursePub.setPic(one.getPic());
        }
        //课程计划，课程计划的章节名称，用于索引
        TeachplanNode teachplanNodes = teachplahMapper.selectList(id);
        String teachplanNodesString = JSON.toJSONString(teachplanNodes);
        //取出
        /*//一级分类
        List<TeachplanNode> firstNodes = teachplanNodes.get(0).getChildren();
        String teachplanString = "";
        for(TeachplanNode firstNode:firstNodes){
            teachplanString = teachplanString + firstNode.getPname()+" ";
            //二级分类
            List<TeachplanNode> secondNodes = firstNode.getChildren();
            for(TeachplanNode secondNode:secondNodes){
                teachplanString = teachplanString + secondNode.getPname()+" ";
            }
        }*/
        coursePub.setTeachplan(teachplanNodesString);
//        coursePub.setTimestamp(new Date());
//        coursePub.setPubTime(new Date());
        return coursePub;
    }
    //更新课程发布状态
    private CourseBase saveCoursePubState(String courseId){
        CourseBase courseBase = courseBaseRepository.findOne(courseId);
        //更新发布状态
        courseBase.setStatus("202002");
        CourseBase save = courseBaseRepository.save(courseBase);
        return save;
    }
}

