package com.xuecheng.framework.domain.learning.response;

import com.xuecheng.framework.domain.learning.XcLearningCourse;
import lombok.Data;
import lombok.ToString;

/**
 * Created by admin on 2018/2/10.
 */
@Data
@ToString
public class XcLearningCourseEx extends XcLearningCourse {

    //排序字段名称
    String sortField;
    //排序类型 desc/asc
    String sortType;
}
