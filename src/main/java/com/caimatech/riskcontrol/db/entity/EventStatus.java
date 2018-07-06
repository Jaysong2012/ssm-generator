package com.caimatech.riskcontrol.db.entity;

public class EventStatus {
    private Long id;

    private Long appId;

    private String eventId;

    private Byte eventStatus;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId == null ? null : eventId.trim();
    }

    public Byte getEventStatus() {
        return eventStatus;
    }

    public void setEventStatus(Byte eventStatus) {
        this.eventStatus = eventStatus;
    }
}