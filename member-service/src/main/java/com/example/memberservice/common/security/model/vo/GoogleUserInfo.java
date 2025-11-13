package com.example.memberservice.common.security.model.vo;

import java.util.Map;

public class GoogleUserInfo implements OAuthUserInfo {

    private final Map<String, Object> attributes;

    public GoogleUserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String getProviderId() {
        return attributes.get("sub").toString();
    }

    @Override
    public String getProvider() {
        return "GOOGLE";
    }

    @Override
    public String getEmail() {
        Object emailObject = attributes.get("email");
        if (emailObject == null) {
            throw new IllegalArgumentException("Email not found in attributes");
        }
        return emailObject.toString();
    }
}
