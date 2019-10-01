package com.xuecheng.framework.domain.learning.request;

import com.xuecheng.framework.model.request.RequestData;
import lombok.Data;
import lombok.ToString;

/**
 * @author Administrator
 * @version 1.0
 * @create 2018-06-30 16:45
 **/
@Data
@ToString
public class QueryChooseCourseRequest extends RequestData {

    String sortField="startTime";
    String sortType="desc";
    String status;
}
