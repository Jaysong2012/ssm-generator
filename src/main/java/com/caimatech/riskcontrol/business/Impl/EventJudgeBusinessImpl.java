package com.caimatech.riskcontrol.business.Impl;

import com.caimatech.riskcontrol.db.entity.RiskEventEvaluateRecord;
import com.caimatech.riskcontrol.db.mapper.EventStatusMapper;
import com.caimatech.riskcontrol.db.mapper.RiskEventEvaluateRecordMapper;
import com.caimatech.riskcontrol.db.serviceImpl.RiskEventEvaluteServiceImpl;
import com.caimatech.riskcontrol.procotol.base.ObjectResponse;
import com.caimatech.riskcontrol.procotol.request.EventJudgeArgs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventJudgeBusinessImpl {

    @Autowired
    private ThreadPoolTaskExecutor threadPoolTaskExecutor;

    @Autowired
    private RiskEventEvaluateRecordMapper riskEventEvaluateRecordMapper;

    @Autowired
    private EventStatusMapper eventStatusMapper;

    @Autowired
    private RiskEventEvaluteServiceImpl riskEventEvaluteService;

    protected static Logger logger = LoggerFactory.getLogger(EventJudgeBusinessImpl.class);

    public ObjectResponse eventJudge(EventJudgeArgs eventJudge, String ip) {
        return null;
    }

    public boolean checkEvent(List<RiskEventEvaluateRecord> records, String eventId) {
        for (RiskEventEvaluateRecord record : records) {
            if (record.getResultCode().equals("rcsfb100") && record.getEventId().equals(eventId)) {
                return true;
            }
        }
        return false;
    }
}
