package com.caimatech.riskcontrol.controller;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.ParseException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.caimatech.riskcontrol.db.serviceImpl.ExampleServiceImpl;
import com.caimatech.riskcontrol.procotol.request.EventJudgeQueryArgs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import com.alibaba.fastjson.JSONObject;
import com.caimatech.riskcontrol.business.Impl.EventJudgeBusinessImpl;
import com.caimatech.riskcontrol.constants.SystemConstants;
import com.caimatech.riskcontrol.http.HttpClientSC;
import com.caimatech.riskcontrol.procotol.base.BaseRequest;
import com.caimatech.riskcontrol.procotol.base.ObjectResponse;
import com.caimatech.riskcontrol.procotol.base.ReturnCode;
import com.caimatech.riskcontrol.procotol.base.ReturnMsg;
import com.caimatech.riskcontrol.procotol.request.EventJudgeArgs;
import com.caimatech.riskcontrol.util.AesEncryptUtil;
import com.caimatech.riskcontrol.util.Md5Util;

@Controller
@RequestMapping("/c")
public class ReuqestController {

    protected static Logger logger = LoggerFactory.getLogger(ReuqestController.class);
    
    @Autowired
    private EventJudgeBusinessImpl eventJudgeBusinessImpl;

    @Autowired
    private ExampleServiceImpl exampleService;
    
    @CrossOrigin(origins="*",maxAge=3600)
    @RequestMapping(value = "/i", method = {RequestMethod.POST,RequestMethod.GET},produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String call(HttpServletRequest request,HttpServletResponse response) throws Exception {
        ObjectResponse objectResponse = new ObjectResponse();
        long requestTime = System.currentTimeMillis();
        
        try{
            request.setCharacterEncoding("UTF-8");
        }catch(Exception e){
        }
        
        String ip = getIpAddr(request);
        String requestBody = "";
        try {
            requestBody = HttpClientSC.getPostBody(request);
        } catch (IOException e) {
            logger.error(e.toString());
        }
        logger.info("/c/a requestBody = {} ip = {} ",requestBody,ip);
        
        if(StringUtils.isEmpty(requestBody)){
            objectResponse.setReturnCode(ReturnCode.EMPTY_REQUEST);
            objectResponse.setReturnMsg(ReturnMsg.EMPTY_REQUEST);
            objectResponse.setCost(System.currentTimeMillis() - requestTime);
            return encryptResponse(objectResponse.toString());
        }
        //解密报文
        String DeString = AesEncryptUtil.decrypt(requestBody, SystemConstants.AES_KEY);
        logger.info("/c/a DeString = {} ip = {} ",DeString,ip);
        
        BaseRequest baseRequest = null;
        try{
            baseRequest = JSONObject.parseObject(DeString, BaseRequest.class);
        }catch(Exception e){
        }
        if(baseRequest == null){
            objectResponse.setReturnCode(ReturnCode.JSON_PARSE_FAIL);
            objectResponse.setReturnMsg(ReturnMsg.JSON_PARSE_FAIL);
            objectResponse.setCost(System.currentTimeMillis() - requestTime);
            return encryptResponse(objectResponse.toString());
        }
        
        String ua = baseRequest.getUa();
        String call = baseRequest.getCall();
        Object args = baseRequest.getArgs();
        String sign = baseRequest.getSign();
        long timestamp = baseRequest.getTimestamp();
        
        if(StringUtils.isEmpty(ua)||StringUtils.isEmpty(call)||StringUtils.isEmpty(sign)||timestamp==0l||args==null){
            objectResponse.setReturnCode(ReturnCode.EMPTY_REQUEST);
            objectResponse.setReturnMsg(ReturnMsg.EMPTY_REQUEST);
            objectResponse.setCost(System.currentTimeMillis() - requestTime);
            return encryptResponse(objectResponse.toString());
        }
        
        boolean signResult = checkSign(ua,call,sign,timestamp);
        
        if(!signResult){
            objectResponse.setReturnCode(ReturnCode.WRONG_SIGN);
            objectResponse.setReturnMsg(ReturnMsg.WRONG_SIGN);
            objectResponse.setCost(System.currentTimeMillis() - requestTime);
            return encryptResponse(objectResponse.toString());
        }
        
        switch(call){
            case "Risk.eventJudge":{
                objectResponse = Risk_eventJudge(args, ip);
                break;
            }
            case "Risk.queryJudgeResult":{
                objectResponse = Risk_queryJudgeResult(args);
                break;
            }
            default:{
                objectResponse.setReturnCode(ReturnCode.WRONG_CALL);
                objectResponse.setReturnMsg(ReturnMsg.WRONG_CALL);
                break;
            }
        }
    
        objectResponse.setCost(System.currentTimeMillis() - requestTime);
        //加密报文
        return encryptResponse(objectResponse.toString());
    }

    @GetMapping("/t")
    @ResponseBody
    public String test(){
        try {
            exampleService.exampleUse();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "test";
    }

    private ObjectResponse Risk_queryJudgeResult(Object args) {
        ObjectResponse objectResponse = new ObjectResponse();
        String argsStr = JSONObject.toJSONString(args);
        EventJudgeQueryArgs requestArgs = null;
        try{
            requestArgs = JSONObject.parseObject(argsStr, EventJudgeQueryArgs.class);
        }catch(Exception e){}

        if(requestArgs == null){
            objectResponse.setReturnCode(ReturnCode.EMPTY_REQUEST);
            objectResponse.setReturnMsg(ReturnMsg.EMPTY_REQUEST);
            return objectResponse;
        }

        if(StringUtils.isEmpty(requestArgs.getUserId())){
            objectResponse.setReturnCode(ReturnCode.EMPTY_REQUEST);
            objectResponse.setReturnMsg(ReturnMsg.EMPTY_REQUEST);
            return objectResponse;
        }
        return null;
    }

    private ObjectResponse Risk_eventJudge(Object args, String ip) {
        ObjectResponse objectResponse = new ObjectResponse();
        String argsStr = JSONObject.toJSONString(args);
        EventJudgeArgs requestArgs = null;
        try{
            requestArgs = JSONObject.parseObject(argsStr, EventJudgeArgs.class);
        }catch(Exception e){}
        
        if(requestArgs == null){
            objectResponse.setReturnCode(ReturnCode.EMPTY_REQUEST);
            objectResponse.setReturnMsg(ReturnMsg.EMPTY_REQUEST);
            return objectResponse;
        }
        
        if(StringUtils.isEmpty(requestArgs.getEventId()) || StringUtils.isEmpty(requestArgs.getSessionKey())){
            objectResponse.setReturnCode(ReturnCode.EMPTY_REQUEST);
            objectResponse.setReturnMsg(ReturnMsg.EMPTY_REQUEST);
            return objectResponse;
        }
        return eventJudgeBusinessImpl.eventJudge(requestArgs, ip);
    }

    protected String getIpAddr(HttpServletRequest request){  
        String ipAddress = request.getHeader("x-forwarded-for");  
            if(ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {  
                ipAddress = request.getHeader("Proxy-Client-IP");  
            }  
            if(ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {  
                ipAddress = request.getHeader("WL-Proxy-Client-IP");  
            }  
            if(ipAddress == null || ipAddress.length() == 0 || "unknown".equalsIgnoreCase(ipAddress)) {  
                ipAddress = request.getRemoteAddr();  
                if(ipAddress.equals("127.0.0.1") || ipAddress.equals("0:0:0:0:0:0:0:1")){  
                    //根据网卡取本机配置的IP  
                    InetAddress inet=null;  
                    try {  
                        inet = InetAddress.getLocalHost();  
                    } catch (UnknownHostException e) {
                        logger.error("UnknownHostException e = {}",e);
                    }  
                    ipAddress= inet.getHostAddress();  
                }  
            }  
            //对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割  
            if(ipAddress!=null && ipAddress.length()>15){ //"***.***.***.***".length() = 15  
                if(ipAddress.indexOf(",")>0){  
                    ipAddress = ipAddress.substring(0,ipAddress.indexOf(","));  
                }  
            }  
            return ipAddress;   
    }

    private boolean checkSign(String ua,String call,String sign,long timestamp){
        switch(ua){
            case "CAIMA_H5_SIGN":{
                return h5Sign(ua,call,sign,timestamp);
            }
            case "CAIMA_APP_SIGN":{
                return appSign(ua,call,sign,timestamp);
            }
        }
        return false;
    }
    
    private boolean h5Sign(String ua,String call,String sign,long timestamp){
        String ori_str = ua+"&"+call+"&"+timestamp+"&"+SystemConstants.CAIMA_H5_SIGN_KEY;
        String sign_str = "";
        try {
            sign_str = Md5Util.md5(ori_str);
        } catch (Exception e) {
        }
        return sign.equals(sign_str);
    }
    
    private boolean appSign(String ua,String call,String sign,long timestamp){
        String ori_str = ua+"&"+call+"&"+timestamp+"&"+SystemConstants.CAIMA_APP_SIGN_KEY;
        String sign_str = "";
        try {
            sign_str = Md5Util.md5(ori_str);
        } catch (Exception e) {
        }
        return sign.equals(sign_str);
    }
    
    private String encryptResponse(String responseStr){
        try {
            return AesEncryptUtil.encrypt(responseStr, SystemConstants.AES_KEY);
        } catch (Exception e) {
            logger.error(e.toString());
        }
        return "";
    }


}
