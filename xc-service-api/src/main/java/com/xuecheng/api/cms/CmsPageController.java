package com.xuecheng.api.cms;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.domain.cms.response.GenerateHtmlResult;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

/**
 * Created by mrt on 2018/3/30.
 */

@RestController
public class CmsPageController implements CmsPageControllerApi {


    @Override
    public CmsPageResult add(@RequestBody CmsPage cmsPage) {
        return null;
    }

    @Override
    public ResponseResult delete(@PathVariable("id") String id) {
        return null;
    }

    @Override
    public CmsPageResult update(@RequestBody CmsPage cmsPage) {
        return null;
    }

    @Override
    public CmsPageResult findById(@PathVariable("id") String id) {
        return null;
    }

    @Override
    public QueryResponseResult findList(@PathVariable("page") int page, @PathVariable("size") int size, QueryPageRequest queryPageRequest) {
        return null;
    }

    @Override
    public GenerateHtmlResult generateHtml(@PathVariable("pageId") String pageId) throws Exception {
        return null;
    }

    @Override
    public GenerateHtmlResult getHtml(@PathVariable("pageId") String pageId) throws IOException {
        return null;
    }

    @Override
    public ResponseResult post(@PathVariable("pageId") String pageId) {
        return null;
    }
}
