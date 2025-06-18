package com.leverx.learningmanagementsystem.web.oauth.token.model;

public record TokenRequest(String tokenUrl, String clientId, String clientSecret) {
}
