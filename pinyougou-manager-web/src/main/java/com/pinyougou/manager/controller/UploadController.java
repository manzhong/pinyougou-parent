package com.pinyougou.manager.controller;

import entity.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import utils.FastDFSClient;

@RestController
public class UploadController {
    @Value("${FILE_SERVER_URL}")
    private String FILE_SERVER_URL;
    @RequestMapping("/upload")
    public Result upload(MultipartFile file){
        //1、取文件的扩展名
        String originalFilename = file.getOriginalFilename();
        String extName = originalFilename.substring(originalFilename.lastIndexOf(".")+1);
        try {
            //创建客户端
            FastDFSClient fastDFSClient=new FastDFSClient("classpath:config/fdfs_client.conf");
            //执行上传
            String path = fastDFSClient.uploadFile(file.getBytes(), extName);
            //拼接返回的url和ip地址,拼成完整的url
            String url=FILE_SERVER_URL+path;
            return new Result(true,url);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,"上传失败");
        }
    }
}
