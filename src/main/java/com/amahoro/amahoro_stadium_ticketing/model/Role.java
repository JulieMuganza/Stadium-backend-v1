package com.amahoro.amahoro_stadium_ticketing.model;


public enum Role {
    USER("Client"),
    ADMIN("Admin");

    private final String value;

    private Role(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
    public String getRole(String value) {
        return this.value;
    }
}
