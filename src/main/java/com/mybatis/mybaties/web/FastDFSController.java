package com.mybatis.mybaties.web;

import com.mybatis.mybaties.entity.FastDFSFile;
import com.mybatis.mybaties.utils.FastDFSClient;
import com.mybatis.mybaties.utils.FileManager;
import com.mybatis.mybaties.utils.ResultUtil;
import org.csource.common.MyException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@RestController
public class FastDFSController {

    private static Logger logger = LoggerFactory.getLogger(FastDFSController.class);

    @PostMapping("/upload")
    public ResultUtil singleFileUpload(@RequestBody MultipartFile file) {
        ResultUtil resultUtil = new ResultUtil();
        if (file.isEmpty()) {
            resultUtil.setCode(1);
            resultUtil.setMsg("文件不存在");
            return resultUtil;
        }
        try {
            // Get the file and save it somewhere
            String path=FileManager.saveFile(file);
            resultUtil.setCode(0);
            resultUtil.setData(path);
            resultUtil.setMsg("上传成功");
        } catch (Exception e) {
            logger.error("upload file failed",e);
        }
        return resultUtil;
    }

    @GetMapping("/downfile")
    public ResponseEntity<byte[]> download(@RequestParam String filePath, String name) throws IOException, MyException {
        String substr = filePath.substring(filePath.indexOf("group"));
        String group = substr.split("/")[0];
        String remoteFileName = substr.substring(substr.indexOf("/")+1);
        String specFileName = name + substr.substring(substr.indexOf("."));
        return FileManager.download(group, remoteFileName,specFileName);
    }
}
