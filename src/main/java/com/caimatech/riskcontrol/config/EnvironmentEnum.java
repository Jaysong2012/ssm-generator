package com.caimatech.riskcontrol.config;

public enum EnvironmentEnum {

    DEBUG("debug",0),TEST("test",1),PRO("pro",2);
    
    public String getEnv() {
        return env;
    }

    public void setEnv(String env) {
        this.env = env;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    private String env;
    
    private int level;
    
    private EnvironmentEnum(String env,int level){
        this.env = env;
        this.level = level;
    }
    
}
