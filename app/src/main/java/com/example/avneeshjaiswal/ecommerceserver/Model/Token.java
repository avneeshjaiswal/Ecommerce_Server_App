package com.example.avneeshjaiswal.ecommerceserver.Model;

/**
 * Created by avneesh jaiswal on 15-Mar-18.
 */

public class Token {
    private String token;
    private boolean isServerToken;

    public Token(String token, boolean isServerToken) {
        this.token = token;
        this.isServerToken = isServerToken;
    }

    public Token() {
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isServerToken() {
        return isServerToken;
    }

    public void setServerToken(boolean serverToken) {
        isServerToken = serverToken;
    }
}
