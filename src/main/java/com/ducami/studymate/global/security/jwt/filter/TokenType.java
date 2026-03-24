package com.ducami.studymate.global.security.jwt.filter;

public enum TokenType {
    ACCESS,
    REFRESH;

    public boolean matches(String tokenType) {
        return this.name().equalsIgnoreCase(tokenType);
    }
}
