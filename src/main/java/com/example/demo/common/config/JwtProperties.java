package com.example.demo.common.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@ConfigurationProperties(prefix = "jwt")
@Component
public class JwtProperties {
    private String secretKey;
    private AccessToken accessToken;
    private RefreshToken refreshToken;
    @Data
    public static class AccessToken{
        private long expiredAt;
    }

    @Data
    public static class RefreshToken{
        private long expiredAt;
    }

}
