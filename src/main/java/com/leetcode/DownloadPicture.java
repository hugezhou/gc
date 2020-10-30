package com.leetcode;

import java.io.*;
import java.net.*;
import java.util.Random;

public class DownloadPicture {

    /**
     * @从制定URL下载文件并保存到指定目录
     * @param filePath 文件将要保存的目录
     * @param method 请求方法，包括POST和GET
     * @param url 请求的路径
     * @return
     */

    public static File saveUrlAs(String url,String filePath,String method){
        //System.out.println("fileName---->"+filePath);
        //创建不同的文件夹目录
        File file=new File(filePath);
        //判断文件夹是否存在
        if (!file.exists())
        {
            //如果文件夹不存在，则创建新的的文件夹
            file.mkdirs();
        }
        FileOutputStream fileOut = null;
        HttpURLConnection conn = null;
        InputStream inputStream = null;
        try
        {
            // 建立链接
            URL httpUrl=new URL(url);
            conn=(HttpURLConnection) httpUrl.openConnection();
            //以Post方式提交表单，默认get方式
            conn.setRequestMethod(method);
            conn.setDoInput(true);
            conn.setDoOutput(true);
            // post方式不能使用缓存
            conn.setUseCaches(false);
            //连接指定的资源
            conn.connect();
            //获取网络输入流
            inputStream=conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(inputStream);
            //判断文件的保存路径后面是否以/结尾
            if (!filePath.endsWith("/")) {
                filePath += "/";
            }
            //写入到文件（注意文件保存路径的后面一定要加上文件的名称）

            int radomInt2 =(int)((Math.random()*9+1)*100000);
            fileOut = new FileOutputStream(filePath+radomInt2+".png");
            BufferedOutputStream bos = new BufferedOutputStream(fileOut);

            byte[] buf = new byte[4096];
            int length = bis.read(buf);
            //保存文件
            while(length != -1)
            {
                bos.write(buf, 0, length);
                length = bis.read(buf);
            }
            bos.close();
            bis.close();
            conn.disconnect();
        } catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("抛出异常！！");
        }

        return file;

    }


    /**
     * @param args
     */
    public static void main(String[] args)
    {
        String photoUrl = "http://media.chinaloyal.cn/1556414112708";   //文件URL地址
        String fileName = photoUrl.substring(photoUrl.lastIndexOf("/"));     //为下载的文件命名
        String filePath = "/Users/zhou/Desktop/upload/pic";      //保存目录
        File file = saveUrlAs(photoUrl, filePath,"GET");

//
//        int radomInt2 =(int)((Math.random()*9+1)*100000);
//        System.out.println(radomInt2);


    }






}


