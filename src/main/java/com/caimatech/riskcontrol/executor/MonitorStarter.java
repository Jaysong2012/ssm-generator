package com.caimatech.riskcontrol.executor;

import org.springframework.beans.factory.annotation.Autowired;

public class MonitorStarter {

    @Autowired
    private ThreadPoolMonitorService threadPoolMonitorService;
    
    public void init(){
        new Thread(threadPoolMonitorService).start();
    }
}
