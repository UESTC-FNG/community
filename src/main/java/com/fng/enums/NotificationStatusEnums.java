package com.fng.enums;

public enum NotificationStatusEnums {
    UNREAD(0),
    READ(1);
    private Integer type;

    NotificationStatusEnums(Integer type) {
        this.type = type;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
