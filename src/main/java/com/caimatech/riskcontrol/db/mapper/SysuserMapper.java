package com.caimatech.riskcontrol.db.mapper;

import com.caimatech.riskcontrol.db.entity.Sysuser;
import com.caimatech.riskcontrol.db.entity.SysuserExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface SysuserMapper {
    long countByExample(SysuserExample example);

    int deleteByExample(SysuserExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Sysuser record);

    int insertSelective(Sysuser record);

    List<Sysuser> selectByExampleWithRowbounds(SysuserExample example, RowBounds rowBounds);

    List<Sysuser> selectByExample(SysuserExample example);

    Sysuser selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Sysuser record, @Param("example") SysuserExample example);

    int updateByExample(@Param("record") Sysuser record, @Param("example") SysuserExample example);

    int updateByPrimaryKeySelective(Sysuser record);

    int updateByPrimaryKey(Sysuser record);
}