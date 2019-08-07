package com.mybatis.mybaties.utils;

import com.mybatis.mybaties.entity.FastDFSFile;
import org.csource.fastdfs.ClientGlobal;
import org.csource.fastdfs.StorageClient;
import org.csource.fastdfs.TrackerClient;
import org.csource.fastdfs.TrackerServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

public class FileManager {

    private static Logger logger = LoggerFactory.getLogger(FastDFSClient.class);

    static {
        try {
            String filePath = new ClassPathResource("fdfs_client.conf").getFile().getAbsolutePath();;
            ClientGlobal.init(filePath);
        } catch (Exception e) {
            logger.error("FastDFS Client Init Fail!",e);
        }
    }

    /**
     * <strong>方法概要： 文件下载</strong> <br>
     * <strong>创建时间： 2016-9-26 上午10:28:21</strong> <br>
     *
     * @param  groupName
     * @param  remoteFileName
     * @return returned value comment here
     * @author Wang Liang
     */
    public static ResponseEntity<byte[]> download(String groupName,
                                                  String remoteFileName, String specFileName) {
        byte[] content = null;
        HttpHeaders headers = new HttpHeaders();
        StorageClient storageClient = null;
        try {
            TrackerClient trackerClient = new TrackerClient();
            TrackerServer trackerServer = trackerClient.getConnection();
            storageClient = new StorageClient(trackerServer, null);
            content = storageClient.download_file(groupName, remoteFileName);
            headers.setContentDispositionFormData("attachment",  new String(specFileName.getBytes("UTF-8"),"iso-8859-1"));
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return new ResponseEntity<byte[]>(content, headers, HttpStatus.CREATED);
    }

    /**
     * @param multipartFile
     * @return
     * @throws IOException
     */
    public static String saveFile(MultipartFile multipartFile) throws IOException {
        String[] fileAbsolutePath={};
        String fileName=multipartFile.getOriginalFilename();
        String ext = fileName.substring(fileName.lastIndexOf(".") + 1);
        if (ext.isEmpty()) {
            ext = "txt";
        }
        byte[] file_buff = null;
        InputStream inputStream=multipartFile.getInputStream();
        if(inputStream!=null){
            int len1 = inputStream.available();
            file_buff = new byte[len1];
            inputStream.read(file_buff);
        }
        inputStream.close();
        FastDFSFile file = new FastDFSFile(fileName, file_buff, ext);
        try {
            fileAbsolutePath = FastDFSClient.upload(file);  //upload to fastdfs
        } catch (Exception e) {
            logger.error("upload file Exception!",e);
        }
        if (fileAbsolutePath==null) {
            logger.error("upload file failed,please upload again!");
        }
        String path=FastDFSClient.getTrackerUrl()+fileAbsolutePath[0]+ "/"+fileAbsolutePath[1];
        return path;
    }

}
