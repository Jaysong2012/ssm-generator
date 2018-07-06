package com.caimatech.riskcontrol.db.mapper;

import com.caimatech.riskcontrol.db.entity.App;

public interface AppMapper {
    int deleteByPrimaryKey(Long id);

    int insert(App record);

    int insertSelective(App record);

    App selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(App record);

    int updateByPrimaryKey(App record);
}