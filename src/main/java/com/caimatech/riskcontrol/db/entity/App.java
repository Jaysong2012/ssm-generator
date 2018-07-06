package com.caimatech.riskcontrol.db.entity;

public class App {
    private Long id;

    private String appName;

    private Byte switcher;

    private String rule;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName == null ? null : appName.trim();
    }

    public Byte getSwitcher() {
        return switcher;
    }

    public void setSwitcher(Byte switcher) {
        this.switcher = switcher;
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule == null ? null : rule.trim();
    }
}