package com.example.demo.src.payment;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "pg")
public class PGAgentProperties {

    private Merchant merchant;
    private Kakaopay kakaopay;

    @Getter
    @Setter
    public static class Merchant{
        private String code;
        private String restApiKey;
        private String restApiSecret;

    }

    @Getter
    @Setter
    public static class Kakaopay{
        private String pgCode;
    }

}
