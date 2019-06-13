package com.caimatech.riskcontrol.db.serviceImpl;

import com.caimatech.riskcontrol.db.entity.Role;
import com.caimatech.riskcontrol.db.entity.RoleExample;
import com.caimatech.riskcontrol.db.mapper.RoleMapper;
import com.caimatech.riskcontrol.db.mapper.SysuserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class ExampleServiceImpl {

    @Autowired
    private SysuserMapper sysuserMapper;

    @Autowired
    private RoleMapper roleMapper;

    public void simpleUse(){
//        Role role = new Role();
//        role.setName("x");
//        role.setPower("{}");
//        role.setRemark("y");
//        role.setStatus(1);
//
//        roleMapper.insertSelective(role);

        Role queryEntity = new Role();
        queryEntity.setStatus(1);
        queryEntity.setName("x");

        List<Role> roleList = roleMapper.selectBySelective(queryEntity);

        roleList.get(0).setName("y");
        roleMapper.updateByPrimaryKeySelective(roleList.get(0));

    }

    public void exampleUse() throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        RoleExample roleExample = new RoleExample();
        roleExample.createCriteria()
                .andCreateTimeBetween(simpleDateFormat.parse("2019-06-01 00:00:00"),simpleDateFormat.parse("2019-06-02 00:00:00"))
                .andCreateTimeGreaterThanOrEqualTo(simpleDateFormat.parse("2019-06-01 12:00:00"))
                .andIdIn(new ArrayList<Integer>(Arrays.asList(1,3,5,7,9)))
                .andPowerIsNotNull()
                .andNameLike("X")
                .andPowerEqualTo("{}");
        RoleExample.Criteria criteria1 = roleExample.createCriteria();
        criteria1.andCreateTimeLessThanOrEqualTo(simpleDateFormat.parse("2019-05-01 00:00:00"))
                .andIdEqualTo(1);
        roleExample.or(criteria1);

        List<Role> roleList = roleMapper.selectByExample(roleExample);

    }
}
