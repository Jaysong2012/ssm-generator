package com.caimatech.riskcontrol.db.serviceImpl;

import com.caimatech.riskcontrol.db.entity.App;
import com.caimatech.riskcontrol.db.entity.EventStatus;
import com.caimatech.riskcontrol.db.entity.RiskEventEvaluateRecord;
import com.caimatech.riskcontrol.db.entity.RiskEventEvaluateRecordWithBLOBs;
import com.caimatech.riskcontrol.db.mapper.RiskEventEvaluateRecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by yang on 2018/6/29.
 */
@Service
public class RiskEventEvaluteServiceImpl {

    @Autowired
    private RiskEventEvaluateRecordMapper riskEventEvaluateRecordMapper;

    public Integer insertSelective(RiskEventEvaluateRecordWithBLOBs record) {
        return riskEventEvaluateRecordMapper.insertSelective(record);
    }

    public RiskEventEvaluateRecord findByCustomerIdAndEventId(RiskEventEvaluateRecordWithBLOBs record) {
        return riskEventEvaluateRecordMapper.findByCustomerIdAndEventId(record);
    }

    public Integer updateByPrimaryKey(RiskEventEvaluateRecordWithBLOBs record) {
        return riskEventEvaluateRecordMapper.updateByPrimaryKeySelective(record);
    }
}
