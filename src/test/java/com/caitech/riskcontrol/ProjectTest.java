package com.caitech.riskcontrol;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.alibaba.fastjson.JSONObject;
import com.mucfc.ftc.tenant.server.common.FtcCaller;

public class ProjectTest {

    @Test
    public void dataTest(){
        Map<String, Object> eventMap = new HashMap<String, Object>();
        eventMap.put("tenantId", FtcCaller.tenantId);
        eventMap.put("appId", FtcCaller.appId);
        String flowNo = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
        eventMap.put("flowNo", flowNo);
        eventMap.put("beginTime", String.valueOf(new Date().getTime()));
        eventMap.put("eventId", "register");
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
        
        System.out.print(JSONObject.toJSONString(eventMap));
    }
    
}
