package com.example.memberservice.member.service.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class RequestURIGenerator {

    @Value("${request.uri.ratings.info}")
    private String ratingUri;

    @Value("${request.uri.tags.info")
    private String tagsUri;

    public String getRatingUri(String memberCode){
        return ratingUri+"/"+memberCode;
    }

    public String gettagsUri(String memberCode){
        return tagsUri+"/me";
    }
}
