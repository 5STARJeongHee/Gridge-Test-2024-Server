package com.example.demo.utils;

import com.example.demo.common.PasswordValidStatus;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ValidationRegexTest {

    @DisplayName("userId 정규 표현식 테스트 - 성공")
    @Test
    void isUserIdSuccess() {
        String userId = "testUser1_.";
        boolean result = ValidationRegex.isRegexNickName(userId);
        Assertions.assertThat(result).isEqualTo(true);
    }

    @DisplayName("userId 정규 표현식 테스트 - 실패 by 특수문자")
    @Test
    void isUserIdFailWith특수문자() {
        String userId = "testUser1_.$%";
        boolean result = ValidationRegex.isRegexNickName(userId);
        Assertions.assertThat(result).isEqualTo(false);
    }
    @DisplayName("email 정규 표현식 테스트")
    @Test
    void isRegexEmail() {
        String testEmail = "test@test.com";
        boolean result = ValidationRegex.isRegexEmail(testEmail);
        Assertions.assertThat(result).isEqualTo(true);
    }

    @Test
    void isRegexPhone() {
        String testPhone = "+821043431234";
        boolean result = ValidationRegex.isRegexPhone(testPhone);
        Assertions.assertThat(result).isEqualTo(true);
    }

    @DisplayName("패스워드 검증 성공")
    @Test
    void isPasswordValidSuccess() {
        String password = "streamjavA8!";
        PasswordValidStatus result = ValidationRegex.isPasswordValid(password);
        Assertions.assertThat(result).isEqualTo(PasswordValidStatus.VALID);
    }

    @DisplayName("패스워드 검증 실패 - 특수문자 없음")
    @Test
    void isPasswordValidFailWith특수문자없음() {
        String password = "streamjavA8";
        PasswordValidStatus result = ValidationRegex.isPasswordValid(password);
        Assertions.assertThat(result).isEqualTo(PasswordValidStatus.FORMAT_NOT_VALID);
    }

    @DisplayName("패스워드 검증 실패 - 대문자 없음")
    @Test
    void isPasswordValidFailWith대문자_미포함() {
        String password = "testpwd!";
        PasswordValidStatus result = ValidationRegex.isPasswordValid(password);
        Assertions.assertThat(result).isEqualTo(PasswordValidStatus.FORMAT_NOT_VALID);
    }

    @DisplayName("패스워드 검증 실패 - 길이 짧음")
    @Test
    void isPasswordValidFailWith길이짧음() {
        String password = "testA!";
        PasswordValidStatus result = ValidationRegex.isPasswordValid(password);
        Assertions.assertThat(result).isEqualTo(PasswordValidStatus.FORMAT_NOT_VALID);
    }

    @DisplayName("패스워드 검증 실패 - 길이 김")
    @Test
    void isPasswordValidFailWith길이김() {
        String password = "testpwdasdasdasdasdasdasdasdasdasdasdasdasA!";
        PasswordValidStatus result = ValidationRegex.isPasswordValid(password);
        Assertions.assertThat(result).isEqualTo(PasswordValidStatus.FORMAT_NOT_VALID);
    }

    @DisplayName("패스워드 검증 실패 - 연속 문자열")
    @Test
    void isPasswordValidFailWith연속_문자열() {
        String password = "tesabcdef1234A!";
        PasswordValidStatus result = ValidationRegex.isPasswordValid(password);
        Assertions.assertThat(result).isEqualTo(PasswordValidStatus.CONTINUOS);
    }

    @DisplayName("패스워드 검증 실패 - 중복 문자열")
    @Test
    void isPasswordValidFailWith중복_문자열() {
        String password = "streamjavAAAAAAA888!";
        PasswordValidStatus result = ValidationRegex.isPasswordValid(password);
        Assertions.assertThat(result).isEqualTo(PasswordValidStatus.DUPLICATE);
    }
}