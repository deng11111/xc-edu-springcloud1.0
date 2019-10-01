package com.xuecheng.api.fs;

import com.xuecheng.framework.domain.filesystem.FileSystem;
import com.xuecheng.framework.domain.filesystem.response.UploadFileResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.framework.model.response.ResponseResult;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by mrt on 2018/4/12.
 */
@RequestMapping("/filesystem")
@Api(value = "文件系统服务接口", description = "文件系统服务接口")
public interface FileSystemControllerApi {
    //上传文件
    @PostMapping("/upload")
    @ResponseBody
    public UploadFileResult upload(@RequestParam("file") MultipartFile file,
                                   @RequestParam(value="businesskey",required=false) String businesskey,
                                   @RequestParam(value="metedata",required=false) String metadata,
                                   @RequestParam(value="filetag",required=false) String filetag
    );

    //查询文件
    @GetMapping("/querylist")
    public QueryResult<FileSystem> findList(@RequestParam("businesskey") String businesskey);

    //删除文件
    @DeleteMapping("/delete")
    public ResponseResult delete(@RequestParam("fileId") String fileId);


}
