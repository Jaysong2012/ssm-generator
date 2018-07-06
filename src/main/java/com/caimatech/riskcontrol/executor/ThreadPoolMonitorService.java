package com.caimatech.riskcontrol.executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

public class ThreadPoolMonitorService implements IThreadPoolMonitorService{

    protected static Logger logger = LoggerFactory.getLogger(ThreadPoolMonitorService.class);
    
    @Autowired
    private ThreadPoolTaskExecutor thredPoolTaskExecutor;
    
    @Override
    public void run() {
        try {
            while (true){
                monitorThreadPool();
                Thread.sleep(60*1000);
            } 
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    
    @Override
    public void monitorThreadPool() {
        StringBuffer strBuff = new StringBuffer();
        strBuff.append("CurrentPoolSize : ").append(thredPoolTaskExecutor.getThreadPoolExecutor().getPoolSize());
        strBuff.append(" - CorePoolSize : ").append(thredPoolTaskExecutor.getThreadPoolExecutor().getCorePoolSize());
        strBuff.append(" - MaximumPoolSize : ").append(thredPoolTaskExecutor.getThreadPoolExecutor().getMaximumPoolSize());
        strBuff.append(" - ActiveTaskCount : ").append(thredPoolTaskExecutor.getThreadPoolExecutor().getActiveCount());
        strBuff.append(" - CompletedTaskCount : ").append(thredPoolTaskExecutor.getThreadPoolExecutor().getCompletedTaskCount());
        strBuff.append(" - TotalTaskCount : ").append(thredPoolTaskExecutor.getThreadPoolExecutor().getTaskCount());
        strBuff.append(" - isTerminated : ").append(thredPoolTaskExecutor.getThreadPoolExecutor().isTerminated());
        logger.debug(strBuff.toString());
    }


}
