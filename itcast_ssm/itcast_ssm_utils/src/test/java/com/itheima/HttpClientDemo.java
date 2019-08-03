package com.itheima;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class HttpClientDemo {

    public static void main(String[] args) throws IOException {

        // 1. 创建HttpClient对象
        // CloseableHttpClient closeableHttpClient = HttpClients.createDefault();
        CloseableHttpClient httpClient = HttpClientBuilder.create().build();

        // 2. 创建一个HttpGet对象，用来发送Get请求 , 需要指定的一个URL
        HttpGet httpGet = new HttpGet("https://imgsa.baidu.com/news/q%3D100/sign=75a413a45bfbb2fb322b5c127f4b2043/9213b07eca806538209dbfea99dda144ac3482d7.jpg") ;

        // 3. 调用HttpClient对象的execute去发送请求，获取结果
        CloseableHttpResponse httpResponse = httpClient.execute(httpGet);

        // 4. 需要从CloseableHttpResponse中获取响应结果
        HttpEntity entity = httpResponse.getEntity();

        // 如果我们发送的请求，返回的是一个文本内容，那么我们可以直接将其转换成一个字符串
//        String result = EntityUtils.toString(entity, "UTF-8");

        // 如果我们发送的请求，返回的是一个非文本内容，我们就需要获取输入流对象，然后从其中读取数据
        InputStream inputStream = entity.getContent();
        FileOutputStream fileOutputStream = new FileOutputStream(new File("D:\\1.jpg")) ;
        IOUtils.copy(inputStream , fileOutputStream) ;

        fileOutputStream.close();
        httpClient.close();

    }

}
