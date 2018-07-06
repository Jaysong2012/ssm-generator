package com.caimatech.riskcontrol.db.mapper;

import com.caimatech.riskcontrol.db.entity.RiskEventEvaluateRecord;
import com.caimatech.riskcontrol.db.entity.RiskEventEvaluateRecordWithBLOBs;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RiskEventEvaluateRecordMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(RiskEventEvaluateRecordWithBLOBs record);

    int insertSelective(RiskEventEvaluateRecordWithBLOBs record);

    RiskEventEvaluateRecordWithBLOBs selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(RiskEventEvaluateRecordWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(RiskEventEvaluateRecordWithBLOBs record);

    int updateByPrimaryKey(RiskEventEvaluateRecord record);

    List<RiskEventEvaluateRecord> selectByUserId(@Param("userId") String userId);

    RiskEventEvaluateRecord findByCustomerIdAndEventId(RiskEventEvaluateRecordWithBLOBs record);
}