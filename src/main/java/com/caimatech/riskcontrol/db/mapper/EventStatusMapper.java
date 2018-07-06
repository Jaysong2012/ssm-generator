package com.caimatech.riskcontrol.db.mapper;

import com.caimatech.riskcontrol.db.entity.App;
import com.caimatech.riskcontrol.db.entity.EventStatus;

import java.util.List;

public interface EventStatusMapper {
    int deleteByPrimaryKey(Long id);

    int insert(EventStatus record);

    int insertSelective(EventStatus record);

    EventStatus selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(EventStatus record);

    int updateByPrimaryKey(EventStatus record);

    List<EventStatus> selectByAppId(Long appId);
}