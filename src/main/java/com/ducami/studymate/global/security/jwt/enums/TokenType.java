package com.ducami.studymate.global.security.jwt.enums;

public enum TokenType {
    ACCESS,
    REFRESH;

    public boolean matches(String tokenType) {
        return this.name().equalsIgnoreCase(tokenType);
    }
}
