package com.example.demo.src.log.entity;

import com.example.demo.src.log.LogCategory;
import org.springframework.http.HttpMethod;

public class Log {
    private long id;
    private String userId;
    private HttpMethod method;
    private String url;
    private LogCategory category;
    private String content;

}
