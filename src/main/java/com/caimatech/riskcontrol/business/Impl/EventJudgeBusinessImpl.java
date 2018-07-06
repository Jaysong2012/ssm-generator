package com.caimatech.riskcontrol.business.Impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.caimatech.riskcontrol.db.entity.App;
import com.caimatech.riskcontrol.db.entity.EventStatus;
import com.caimatech.riskcontrol.db.entity.RiskEventEvaluateRecord;
import com.caimatech.riskcontrol.db.entity.RiskEventEvaluateRecordWithBLOBs;
import com.caimatech.riskcontrol.db.mapper.AppMapper;
import com.caimatech.riskcontrol.db.mapper.EventStatusMapper;
import com.caimatech.riskcontrol.db.mapper.RiskEventEvaluateRecordMapper;
import com.caimatech.riskcontrol.db.serviceImpl.RiskEventEvaluteServiceImpl;
import com.caimatech.riskcontrol.procotol.request.EventJudgeQueryArgs;
import com.caimatech.riskcontrol.util.DateTimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.caimatech.riskcontrol.procotol.base.ObjectResponse;
import com.caimatech.riskcontrol.procotol.base.ReturnCode;
import com.caimatech.riskcontrol.procotol.base.ReturnMsg;
import com.caimatech.riskcontrol.procotol.request.EventJudgeArgs;
import com.caimatech.riskcontrol.procotol.response.bean.PolicySet;
import com.caimatech.riskcontrol.procotol.response.bean.RiskResult;
import com.mucfc.ftc.aps.base.ServiceResponse;
import com.mucfc.ftc.aps.sdk.exception.ServiceRequestException;
import com.mucfc.ftc.tenant.server.common.FtcCaller;
import org.springframework.util.CollectionUtils;

@Service
public class EventJudgeBusinessImpl {

    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Autowired
    private RiskEventEvaluateRecordMapper riskEventEvaluateRecordMapper;

    @Autowired
    private AppMapper appMapper;

    @Autowired
    private EventStatusMapper eventStatusMapper;

    @Autowired
    private RiskEventEvaluteServiceImpl riskEventEvaluteService;

    protected static Logger logger = LoggerFactory.getLogger(EventJudgeBusinessImpl.class);
    
    public ObjectResponse eventJudge(EventJudgeArgs eventJudge, String ip) {
        ObjectResponse objectResponse = new ObjectResponse();
        
        String serviceId = "risk.antifraud.event.decision"; // 平台提供的"反欺诈.风险事件评估接口"
        
        String flowNo = generalFlowNo(eventJudge.getUserId(), "lyd");
        
        eventJudge.setTenantId(FtcCaller.tenantId);
        eventJudge.setAppId(FtcCaller.appId);
        eventJudge.setFlowNo(flowNo);

        eventJudge.setIp(ip);
        
        // 组装业务参数
        Map<String, String> bizContentMap = new HashMap<String, String>();
        
        // 参数'event'
        bizContentMap.put("event", JSONObject.toJSONString(eventJudge));

        // 调用服务接口
        ServiceResponse serviceResponse = null;
        try {
            serviceResponse = FtcCaller.callService(FtcCaller.appId, serviceId, bizContentMap,null);
            logger.info(">>> response: " + JSONObject.toJSONString(serviceResponse));
        } catch (ServiceRequestException e) {
            logger.error("",e);
        }

        final ServiceResponse response = serviceResponse;
        threadPoolTaskExecutor.execute(() -> {
            insertToDB(eventJudge, response);
        });
        
        if (!serviceResponse.getSuccess()) {
            logger.error(">>> request execute fail, errorCode: {}, errorMsg: {}", serviceResponse.getErrorCode(), serviceResponse.getErrorMsg());
            objectResponse.setReturnCode(ReturnCode.SUCCESS);
            objectResponse.setReturnMsg(ReturnMsg.SUCCESS);
            return objectResponse;
        }
        
        String bizContent = serviceResponse.getBizContent();
        RiskResult riskResult = JSONObject.parseObject(bizContent, RiskResult.class);
        
        String resultDetail = riskResult.getResultDetail();
        
        PolicySet policySet = JSONObject.parseObject(resultDetail, PolicySet.class);
        objectResponse.setReturnCode(ReturnCode.SUCCESS);
        objectResponse.setReturnMsg(ReturnMsg.SUCCESS);
        objectResponse.setResponse(riskResult);
        return objectResponse;
    }

    private String generalFlowNo(String customerId ,String app ){
        return app+"_"+customerId+"_"+DateTimeUtil.getMinuteDateTime();
    }

    public void insertToDB(EventJudgeArgs eventJudge, ServiceResponse serviceResponse){
        RiskEventEvaluateRecordWithBLOBs record = new RiskEventEvaluateRecordWithBLOBs();
        record.setFlowNo(eventJudge.getFlowNo());
        record.setUserName(eventJudge.getUserName());
        record.setUserId(eventJudge.getUserId());
        record.setIdNo(eventJudge.getUseridNo());
        record.setMobileNo(eventJudge.getUserMobileNo());
        record.setSessionKey(eventJudge.getSessionKey());
        record.setEventId(eventJudge.getEventId());
        record.setEventTime(DateTimeUtil.parse(eventJudge.getBeginTime(), "yyyy-MM-dd HH:mm:ss"));
        record.setArgsInfo(JSONObject.toJSONString(eventJudge));
        if(!serviceResponse.getSuccess()){
            record.setSuccess((byte)0);
            record.setErrorCode(serviceResponse.getErrorCode()== null? "" : serviceResponse.getErrorCode());
            record.setErrorMsg(serviceResponse.getErrorMsg() == null ? "" : serviceResponse.getErrorMsg());
        }else{
            String bizContent = serviceResponse.getBizContent();
            RiskResult riskResult = JSONObject.parseObject(bizContent, RiskResult.class);
            record.setSuccess((byte)1);
            record.setResultCode(riskResult.getResultCode());
            record.setResultDesc(riskResult.getResultDesc());
            String resultDetail = riskResult.getResultDetail();
            record.setResultDetail(resultDetail);
        }

        RiskEventEvaluateRecord riskEventEvaluateRecord = riskEventEvaluteService.findByCustomerIdAndEventId(record);
        if(riskEventEvaluateRecord == null){
            riskEventEvaluteService.insertSelective(record);
        }else{
            record.setId(riskEventEvaluateRecord.getId());
            riskEventEvaluteService.updateByPrimaryKey(record);
        }


    }

    public ObjectResponse queryJudgeResult(EventJudgeQueryArgs requestArgs) {
        ObjectResponse objectResponse = new ObjectResponse();
        //check switch
        App app = appMapper.selectByPrimaryKey(1L);
        if(app == null || app.getSwitcher() == 1){
            objectResponse.setReturnCode(ReturnCode.SUCCESS);
            objectResponse.setReturnMsg(ReturnMsg.SUCCESS);
            objectResponse.setResponse("pass");
            return objectResponse;
        }

        List<RiskEventEvaluateRecord> records = riskEventEvaluateRecordMapper.selectByUserId(requestArgs.getUserId());
        if(CollectionUtils.isEmpty(records)){
            objectResponse.setReturnCode(ReturnCode.SUCCESS);
            objectResponse.setReturnMsg(ReturnMsg.SUCCESS);
            objectResponse.setResponse("pass");
            return objectResponse;
        }

        if(app.getRule().equals("one")){
            List<RiskEventEvaluateRecord> results = records.stream()
                    .filter(x -> x.getResultCode()!=null && x.getResultCode().equals("RCSFB100"))
                    .collect(Collectors.toList());
            if(CollectionUtils.isEmpty(results)){
                objectResponse.setReturnCode(ReturnCode.SUCCESS);
                objectResponse.setReturnMsg(ReturnMsg.SUCCESS);
                objectResponse.setResponse("pass");
                return objectResponse;
            }
            objectResponse.setReturnCode(ReturnCode.SUCCESS);
            objectResponse.setReturnMsg(ReturnMsg.SUCCESS);
            objectResponse.setResponse("fail");
            return objectResponse;

        }


        List<EventStatus> events = eventStatusMapper.selectByAppId(app.getId());
        List<String> eventIds = events.stream()
                .filter(x -> x.getEventStatus() == 0)
                .map(x -> x.getEventId())
                .collect(Collectors.toList());

        for(String eventId : eventIds){
            if(!checkEvent(records, eventId)){
                objectResponse.setReturnCode(ReturnCode.SUCCESS);
                objectResponse.setReturnMsg(ReturnMsg.SUCCESS);
                objectResponse.setResponse("fail");
                return objectResponse;
            }
        }

        objectResponse.setReturnCode(ReturnCode.SUCCESS);
        objectResponse.setReturnMsg(ReturnMsg.SUCCESS);
        objectResponse.setResponse("pass");
        return objectResponse;
    }

    public boolean checkEvent(List<RiskEventEvaluateRecord> records, String eventId){
        for(RiskEventEvaluateRecord record : records){
            if(record.getResultCode().equals("rcsfb100") && record.getEventId().equals(eventId)){
                return true;
            }
        }
        return false;
    }
}
