package com.xuecheng.api.portalview;

import com.xuecheng.framework.client.XcServiceList;
import lombok.Getter;
import org.apache.servicecomb.provider.pojo.RpcReference;
import org.springframework.stereotype.Component;

/**
 * @author Administrator
 * @version 1.0
 * @create 2018-08-22 17:04
 **/
@Component
@Getter
public class PortalViewApiFacade {

    @RpcReference(microserviceName="xc-service-portalview",schemaId = XcServiceList.XC_SERVICE_PORTALVIEW)
    ViewCourseControllerApi viewCourseControllerApi;

}
