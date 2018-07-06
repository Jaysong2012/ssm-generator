package com.caimatech.riskcontrol.http;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpClientSC {

    protected static Logger logger = LoggerFactory.getLogger(HttpClientSC.class);
    
    public static String getPostBody(HttpServletRequest request) throws IOException{
        InputStream is = request.getInputStream();
        int contentLen = request.getContentLength();
        if (contentLen > 0) {
            int readLen = 0;
            int readLengthThisTime = 0;
            byte[] message = new byte[contentLen];
            try {
                while (readLen != contentLen) {
                    readLengthThisTime = is.read(message, readLen, contentLen - readLen);
                    if (readLengthThisTime == -1) {// Should not happen.
                        break;
                    }
                    readLen += readLengthThisTime;
                }
                return new String(message);
            } catch (IOException e) {
            }
        }
        return "";
    }
    
    /**
     * Method to flush a String as response.
     * @param response
     * @param responseContent
     * @throws IOException
     */
    public static void flushResponse(HttpServletResponse response, String responseContent) {
        response.setCharacterEncoding("UTF-8");
        try {
            PrintWriter writer = response.getWriter();
            writer.write(responseContent);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public static String xmlUrlPost(String urlStr ,String xml){
        StringBuilder reponseXml = new StringBuilder();
        HttpURLConnection  conn = null;
        try{
            URL url = new URL(urlStr);
            conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(5* 1000);
            conn.setDoOutput(true);//允许输出   
            conn.setUseCaches(false);//不使用Cache
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");//维持长连接   
            conn.setRequestProperty("Charset", "UTF-8");
            conn.setRequestProperty("Content-Length", String.valueOf(xml.length()));  
            conn.setRequestProperty("Content-Type", "text/xml; charset=UTF-8");  

            OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());

            out.write(new String(xml.getBytes("UTF-8")));
            out.flush();
            out.close();
            BufferedReader br = new BufferedReader(new InputStreamReader(conn
                    .getInputStream()));
            String line = "";
            for (line = br.readLine(); line != null; line = br.readLine()) {
                reponseXml.append(line);
            }
            br.close();
        } catch (MalformedURLException e) {
            logger.error("MalformedURLException e= {}",e);
        } catch (IOException e) {
            logger.error("IOException e = {} ",e);
        }finally{
            if(conn!=null){
                conn.disconnect();
            }
        }
        return reponseXml.toString();
    }
    
    public static String httpPostFile(String url ,String path,String customerId,String merchantId
            ,String fileType,String fileName){
        CloseableHttpClient httpclient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        RequestConfig requestConfig = RequestConfig.custom()
                .setSocketTimeout(12000)
                .setConnectTimeout(12000)
                .setConnectionRequestTimeout(12000)
                .build();
        httpPost.setConfig(requestConfig);
        if(path!=null){
            FileBody file = new FileBody(new File(path));
            StringBody customerIdBody = new StringBody(customerId, ContentType.TEXT_PLAIN);
            StringBody merchantIdBody = new StringBody(merchantId, ContentType.TEXT_PLAIN);
            StringBody fileTypeBody = new StringBody(fileType, ContentType.TEXT_PLAIN);
            StringBody fileNameBody = new StringBody(fileName, ContentType.TEXT_PLAIN);

            HttpEntity reqEntity = MultipartEntityBuilder.create()
                    .addPart("file", file)
                    .addPart("customerId", customerIdBody)
                    .addPart("merchantId", merchantIdBody)
                    .addPart("fileType", fileTypeBody)
                    .addPart("fileName", fileNameBody)
                    .build();

            logger.debug("reqEntity = {}",reqEntity);
            httpPost.setEntity(reqEntity);
            CloseableHttpResponse response = null;
            try {
                response = httpclient.execute(httpPost);
                logger.debug("response ="+response.getEntity()+"   getStatusLine =="+response.getStatusLine().getStatusCode());
                if (response.getStatusLine().getStatusCode() == 200) {
                     String str = "";
                     try {
                         /**读取服务器返回过来的json字符串数据**/
                         str = EntityUtils.toString(response.getEntity());
                         return str;
                     } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } catch (ClientProtocolException e) {
                logger.error(" ClientProtocolException {}",e);
            } catch (IOException e) {
                logger.error(" IOException {}",e);
            }finally {
                // 关闭连接,释放资源
                try {
                    logger.debug("HttpPostFile close");
                    if(response != null){
                        response.close();
                    }
                    httpclient.close();
                } catch (IOException e) {
                    logger.error(" IOException {}",e);
                }
            }
        }
        return null;
    }
    
}
