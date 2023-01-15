package com.yuechu.controller;

import com.yuechu.common.R;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.UUID;

//处理上传文件

@RestController
@RequestMapping("/common")
public class CommonController {

    @Value("${reggie.path}")
    private String basePath;
    @PostMapping("/upload")
    public R<String> upload(MultipartFile file) throws IOException {
        //file是一个临时文件，需要转存到，否则本次请求完成后就被会删除
        //原始文件名  不推荐
        String originalFilename = file.getOriginalFilename();
        //动态截取后缀名--》 jpg
        String suffix =  originalFilename.substring(originalFilename.lastIndexOf("."));
        //使用UUID随机生成文件名，防止文件重名被覆盖
        String fileName = UUID.randomUUID().toString()+suffix;

        //创建一个目录对象
        File dirFile = new File(basePath);
        //判断目录是否存在，不存在则创建
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }

        try {
            file.transferTo(new File(basePath+fileName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (IllegalStateException e) {
            throw new RuntimeException(e);
        }
        return R.success(fileName);
    }


    @GetMapping("/download")
    public R<String> upload(String name, HttpServletResponse response) {


        try {
            //输入流，通过输入流读取文件内容
            FileInputStream fileInputStream = new FileInputStream(new File(basePath + name));

            //输出流，通过输出流把文件写会浏览器，在浏览器展示图片
            ServletOutputStream outputStream = response.getOutputStream();


            response.setContentType("image/jepg");

            int len = 0;
            byte[] bytes = new byte[1024];

            while ((len = fileInputStream.read(bytes))!=-1){
                outputStream.write(bytes,0,len);
                outputStream.flush();
            }
//            关闭资源
            outputStream.close();
            fileInputStream.close();

            return R.success("ss");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }


    }


}
