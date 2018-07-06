package com.caimatech.riskcontrol.controller;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.caimatech.riskcontrol.procotol.request.EventJudgeQueryArgs;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.caimatech.riskcontrol.business.Impl.EventJudgeBusinessImpl;
import com.caimatech.riskcontrol.constants.SystemConstants;
import com.caimatech.riskcontrol.http.HttpClientSC;
import com.caimatech.riskcontrol.procotol.base.BaseRequest;
import com.caimatech.riskcontrol.procotol.base.ObjectResponse;
import com.caimatech.riskcontrol.procotol.base.ReturnCode;
import com.caimatech.riskcontrol.procotol.base.ReturnMsg;
import com.caimatech.riskcontrol.procotol.request.EventJudgeArgs;
import com.caimatech.riskcontrol.procotol.response.bean.RiskResult;
import com.caimatech.riskcontrol.util.AesEncryptUtil;
import com.caimatech.riskcontrol.util.Md5Util;
import com.mucfc.ftc.aps.base.ServiceResponse;
import com.mucfc.ftc.aps.sdk.exception.ServiceRequestException;
import com.mucfc.ftc.tenant.server.common.FtcCaller;

@Controller
@RequestMapping("/c")
public class ReuqestController {

    protected static Logger logger = LoggerFactory.getLogger(ReuqestController.class);
    
    @Autowired
    private EventJudgeBusinessImpl eventJudgeBusinessImpl;
    
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
        return eventJudgeBusinessImpl.queryJudgeResult(requestArgs);
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

    @CrossOrigin(origins="*",maxAge=3600)
    @RequestMapping(value = "/t", method = {RequestMethod.POST,RequestMethod.GET},produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String test(HttpServletRequest request,HttpServletResponse response) throws Exception {
        
        ObjectResponse objectResponse = new ObjectResponse();
        
        String serviceId = "risk.antifraud.event.decision"; // 平台提供的"反欺诈.风险事件评估接口"

        // 组装业务参数
        Map<String, String> bizContentMap = new HashMap<String, String>();
        Map<String, Object> eventMap = new HashMap<String, Object>();
        eventMap.put("tenantId", FtcCaller.tenantId);
        eventMap.put("appId", FtcCaller.appId);
        String flowNo = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
        eventMap.put("flowNo", flowNo);
        eventMap.put("beginTime", String.valueOf(new Date().getTime()));
        eventMap.put("eventId", "xxxxxxx");
        eventMap.put("userMobileNo", "18675534575");
        eventMap.put("userName", "张XX");
        eventMap.put("useridNo", "440526199412121111");
        eventMap.put("useridType", "01");
        eventMap.put("userContactPhone", "18800000000");
        eventMap.put("longitude", "130.23");
        eventMap.put("latitude", "23.28");
        eventMap.put("organizationAddress", "广东省深圳市南山区科苑路1888号");
        eventMap.put("accountAddressProvince", "广东省");
        eventMap.put("accountAddressCity", "深圳市");
        eventMap.put("accountAddressCounty", "南山区");
        eventMap.put("accountAddressStreet", "科苑路1888号");
        eventMap.put("homeAddress", "广东省深圳市南山区前湾一路18号");
        eventMap.put("homeProvince", "广东省");
        eventMap.put("homeCity", "深圳市");
        eventMap.put("homeCounty", "南山区");
        eventMap.put("homeStreet", "前湾一路18号");

        // 参数'event'
        bizContentMap.put("event", JSONObject.toJSONString(eventMap));


        // 调用服务接口
        ServiceResponse serviceResponse = null;
        try {
            serviceResponse = FtcCaller.callService(FtcCaller.appId, serviceId, bizContentMap,null);
            logger.info(">>> response: " + JSONObject.toJSONString(serviceResponse));
        } catch (ServiceRequestException e) {
            logger.error("",e);
        }
        
        if (!serviceResponse.getSuccess()) {
            logger.error(">>> request execute fail, errorCode: {}, errorMsg: {}", serviceResponse.getErrorCode(), serviceResponse.getErrorMsg());
            return "fail";
        }
        
        String bizContent = serviceResponse.getBizContent();
        RiskResult riskResult = JSONObject.parseObject(bizContent, RiskResult.class);
        
        objectResponse.setReturnCode(ReturnCode.SUCCESS);
        objectResponse.setReturnMsg(ReturnMsg.SUCCESS);
        objectResponse.setResponse(riskResult);
        return objectResponse.toString();
    }

}
