package com.xuecheng.manage_course.client;

import com.xuecheng.api.cms.CmsCourseControllerApi;
import com.xuecheng.framework.client.XcServiceList;
import org.springframework.cloud.netflix.feign.FeignClient;

/**
 * Created by mrt on 2018/4/20.
 */
@FeignClient(value = XcServiceList.XC_SERVICE_MANAGE_CMS)
public interface CmsCourseClient extends CmsCourseControllerApi {

}
