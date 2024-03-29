package com.example.demo.common.jasypt;

import com.example.demo.common.config.JasyptConfig;
import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.jasypt.encryption.StringEncryptor;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

@ActiveProfiles(value = {"prod"})
@Import(value = {JasyptConfig.class})
@SpringBootTest
public class JasyptTest {
    private final Logger log = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private StringEncryptor jasyptStringEncryptor;

    @Value("${jasypt.encryptor.password}")
    private String JASYPT_PASSWORD;
    @Value("${spring.OAuth2.google.client-id}")
    private String GOOGLE_CLIENT_ID;
    @Value("${spring.OAuth2.google.client-secret}")
    private String GOOGLE_CLIENT_SECRET;

    @Value("${spring.OAuth2.kakao.client-id}")
    private String KAKAO_CLIENT_ID;
    @Value("${jwt.secret-key}")
    private String JWT_SECRET_KEY;
    @Value("${spring.datasource.url}")
    private String DATASOURCE_URL;
    @Value("${spring.datasource.username}")
    private String DATASOURCE_USERNAME;
    @Value("${spring.datasource.password}")
    private String DATASOURCE_PASSWORD;

    @Value("${pg.merchant.code}")
    private String MERCHANT_CODE;
    @Value("${pg.merchant.rest-api-key}")
    private String MERCHANT_API_KEY;
    @Value("${pg.merchant.rest-api-secret}")
    private String MERCHANT_API_SECRET;

    @Value("${pg.kakaopay.pg-code}")
    private String MERCHANT_KAKAOPAY_CODE;

    @DisplayName("jasypt 암호화 정보 생성")
    @Test
    void generateEncryptedInfo() {

        String googleClientId = jasyptStringEncryptor.encrypt(GOOGLE_CLIENT_ID);
        String decryptGoogleClientId = jasyptStringEncryptor.decrypt(googleClientId);

        String googleClientSecret = jasyptStringEncryptor.encrypt(GOOGLE_CLIENT_SECRET);
        String decryptGoogleClientSecret = jasyptStringEncryptor.decrypt(googleClientSecret);

        String jwtSecretKey = jasyptStringEncryptor.encrypt(JWT_SECRET_KEY);
        String jdecryptJwtSecretKey = jasyptStringEncryptor.decrypt(jwtSecretKey);

        String datasourceUrl = jasyptStringEncryptor.encrypt(DATASOURCE_URL);
        String decryptDatasourceUrl = jasyptStringEncryptor.decrypt(datasourceUrl);

        String datasourceUsername = jasyptStringEncryptor.encrypt(DATASOURCE_USERNAME);
        String decryptDatasourceUsername = jasyptStringEncryptor.decrypt(datasourceUsername);

        String datasourcePassword = jasyptStringEncryptor.encrypt(DATASOURCE_PASSWORD);
        String decryptDatasourcePassword = jasyptStringEncryptor.decrypt(datasourcePassword);

        String kakaoClientId = jasyptStringEncryptor.encrypt(KAKAO_CLIENT_ID);
        String decryptKakoClientId = jasyptStringEncryptor.decrypt(kakaoClientId);

        Assertions.assertEquals(GOOGLE_CLIENT_ID, decryptGoogleClientId);
        Assertions.assertEquals(GOOGLE_CLIENT_SECRET, decryptGoogleClientSecret);
        Assertions.assertEquals(JWT_SECRET_KEY, jdecryptJwtSecretKey);
        Assertions.assertEquals(DATASOURCE_URL, decryptDatasourceUrl);
        Assertions.assertEquals(DATASOURCE_USERNAME, decryptDatasourceUsername);
        Assertions.assertEquals(DATASOURCE_PASSWORD, decryptDatasourcePassword);
        Assertions.assertEquals(KAKAO_CLIENT_ID, decryptKakoClientId);

        log.debug(" GOOGLE_CLIENT_ID: [" + GOOGLE_CLIENT_ID + "] has encrypted: " + "ENC("+googleClientId+")");
        log.debug(" GOOGLE_CLIENT_SECRET: [" + GOOGLE_CLIENT_SECRET + "] has encrypted: " + "ENC("+googleClientSecret+")");
        log.debug(" KAKAO_CLIENT_ID: [" + KAKAO_CLIENT_ID + "] has encrypted: " + "ENC("+kakaoClientId+")");
        log.debug(" JWT_SECRET_KEY: [" + JWT_SECRET_KEY + "] has encrypted: " + "ENC("+jwtSecretKey+")");
        log.debug(" DATASOURCE_URL: [" + DATASOURCE_URL + "] has encrypted: " + "ENC("+datasourceUrl+")");
        log.debug(" DATASOURCE_USERNAME: [" + DATASOURCE_USERNAME + "] has encrypted: " + "ENC("+datasourceUsername+")");
        log.debug(" DATASOURCE_PASSWORD: [" + DATASOURCE_PASSWORD + "] has encrypted: " + "ENC("+datasourcePassword+")");

        log.debug(" MERCHANT_CODE: [" + MERCHANT_CODE + "] has encrypted: " + "ENC("+jasyptStringEncryptor.encrypt(MERCHANT_CODE)+")");
        log.debug(" MERCHANT_API_KEY: [" + MERCHANT_API_KEY + "] has encrypted: " + "ENC("+jasyptStringEncryptor.encrypt(MERCHANT_API_KEY)+")");
        log.debug(" MERCHANT_API_SECRET: [" + MERCHANT_API_SECRET + "] has encrypted: " + "ENC("+jasyptStringEncryptor.encrypt(MERCHANT_API_SECRET)+")");
        log.debug(" MERCHANT_KAKAOPAY_CODE: [" + MERCHANT_KAKAOPAY_CODE + "] has encrypted: " + "ENC("+jasyptStringEncryptor.encrypt(MERCHANT_KAKAOPAY_CODE)+")");



    }

}
