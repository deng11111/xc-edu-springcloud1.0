package com.xuecheng.api.id;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * Created by mrt on 2018/3/27.
 */
public interface IdControllerApi {
    @PostMapping("/generateid/{datacenterId}")
    public Long generateId(@PathVariable("datacenterId") String datacenterId);
}
