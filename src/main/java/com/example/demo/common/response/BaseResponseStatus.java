package com.example.demo.common.response;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * 에러 코드 관리
 */
@Getter
public enum BaseResponseStatus {
    /**
     * 200 : 요청 성공
     */
    SUCCESS(true, HttpStatus.OK.value(), "요청에 성공하였습니다."),


    /**
     * 400 : Request, Response 오류
     */

    USERS_EMPTY_EMAIL(false, HttpStatus.BAD_REQUEST.value(), "이메일을 입력해주세요."),
    USERS_EMPTY_NAME(false, HttpStatus.BAD_REQUEST.value(), "성명을 입력해주세요."),
    USERS_EMPTY_NICKNAME(false, HttpStatus.BAD_REQUEST.value(), "닉네임을 입력해주세요."),
    USERS_EMPTY_BIRTHDAY(false, HttpStatus.BAD_REQUEST.value(), "생일을 입력해주세요."),
    USERS_EMPTY_ID(false, HttpStatus.BAD_REQUEST.value(), "이메일 또는 휴대폰 번호를 입력해주세요."),
    USERS_EMPTY_USER_NICKNAME(false, HttpStatus.BAD_REQUEST.value(), "사용자 이름을 입력해주세요."),
    USERS_EMPTY_PHONE_NUMBER(false, HttpStatus.BAD_REQUEST.value(), "휴대폰 번호를 입력해주세요."),
    USERS_EMPTY_PASSWORD(false, HttpStatus.BAD_REQUEST.value(), "비밀번호를 입력해주세요."),
    USERS_EMPTY_CONSENT_AGREED(false, HttpStatus.BAD_REQUEST.value(), "동의 약관을 확인해주십시오"),
    TEST_EMPTY_COMMENT(false, HttpStatus.BAD_REQUEST.value(), "코멘트를 입력해주세요."),
    POST_USERS_INVALID_EMAIL(false, HttpStatus.BAD_REQUEST.value(), "이메일 형식을 확인해주세요."),
    POST_USERS_INVALID_PASSOWRD_FORMAT(false, HttpStatus.BAD_REQUEST.value(), "비밀번호는 영문으로 소문자, 대문자, 특수문자 숫자를 포함하여 7자 이상이어야 합니다."),
    POST_USERS_INVALID_PASSOWRD_DUPLICATE(false, HttpStatus.BAD_REQUEST.value(), "비밀번호에 동일한 문자를 연속해서 사용할 수 없습니다."),
    POST_USERS_INVALID_PASSOWRD_CONTINUOS(false, HttpStatus.BAD_REQUEST.value(), "비밀번호에 연속된 문자를 사용할 수 없습니다."),
    POST_USERS_INVALID_PHONE_NUMBER(false, HttpStatus.BAD_REQUEST.value(), "휴대폰 번호 형식이 맞지 않습니다. 하이픈을 제거한 국제번호 형식으로 입력해주십시오"),
    POST_USERS_EXISTS_USER_NICKNAME(false,HttpStatus.BAD_REQUEST.value(),"중복된 사용자 이름입니다."),
    POST_USERS_EXISTS_EMAIL(false,HttpStatus.BAD_REQUEST.value(),"중복된 이메일입니다."),
    POST_USERS_EXISTS_PHONE_NUMBER(false,HttpStatus.BAD_REQUEST.value(),"중복된 휴대폰 번호 입니다."),
    POST_TEST_EXISTS_MEMO(false,HttpStatus.BAD_REQUEST.value(),"중복된 메모입니다."),

    RESPONSE_ERROR(false, HttpStatus.NOT_FOUND.value(), "값을 불러오는데 실패하였습니다."),

    DUPLICATED_EMAIL(false, HttpStatus.BAD_REQUEST.value(), "중복된 이메일입니다."),
    DUPLICATED_USER(false, HttpStatus.BAD_REQUEST.value(), "이미 등록된 계정입니다."),
    INVALID_MEMO(false,HttpStatus.NOT_FOUND.value(), "존재하지 않는 메모입니다."),
    FAILED_TO_LOGIN(false,HttpStatus.NOT_FOUND.value(),"없는 아이디거나 비밀번호가 틀렸습니다."),
    EMPTY_JWT(false, HttpStatus.UNAUTHORIZED.value(), "JWT를 입력해주세요."),
    INVALID_JWT(false, HttpStatus.UNAUTHORIZED.value(), "유효하지 않은 JWT입니다."),
    INVALID_USER_JWT(false,HttpStatus.FORBIDDEN.value(),"권한이 없는 유저의 접근입니다."),
    NOT_FIND_USER(false,HttpStatus.NOT_FOUND.value(),"일치하는 유저가 없습니다."),
    NOT_FIND_REFRESH_TOKEN(false, HttpStatus.FORBIDDEN.value(), "refresh token이 없거나 만료되었습니다."),
    INVALID_OAUTH_TYPE(false, HttpStatus.FORBIDDEN.value(), "알 수 없는 소셜 로그인 형식입니다."),
    INVALID_PASSOWRD(false, HttpStatus.BAD_REQUEST.value(), "아이디 또는 패스워드가 일치하지 않습니다."),
    INACTIVE_USER(false, HttpStatus.FORBIDDEN.value(), "탈퇴한 유저입니다."),
    LOCK_USER(false, HttpStatus.FORBIDDEN.value(), "계정이 잠겼습니다."),
    EXPIRE_USER(false, HttpStatus.FORBIDDEN.value(), "계정이 만료되었습니다."),
    EXPIRE_AUTHENTICATION(false, HttpStatus.FORBIDDEN.value(), "인증이 만료되었습니다."),

    /** Order
     *
     */
    POST_EMPTY_PGCODE(false, HttpStatus.BAD_REQUEST.value(), "PG 사 정보가 없습니다."),
    POST_EMPTY_PAYMETHOD(false, HttpStatus.BAD_REQUEST.value(), "결제 방식 정보가 없습니다."),
    /**
     * 500 :  Database, Server 오류
     */
    DATABASE_ERROR(false, HttpStatus.INTERNAL_SERVER_ERROR.value(), "데이터베이스 연결에 실패하였습니다."),
    SERVER_ERROR(false, HttpStatus.INTERNAL_SERVER_ERROR.value(), "서버와의 연결에 실패하였습니다."),
    PASSWORD_ENCRYPTION_ERROR(false, HttpStatus.INTERNAL_SERVER_ERROR.value(), "비밀번호 암호화에 실패하였습니다."),
    PASSWORD_DECRYPTION_ERROR(false, HttpStatus.INTERNAL_SERVER_ERROR.value(), "비밀번호 복호화에 실패하였습니다."),


    MODIFY_FAIL_USERNAME(false,HttpStatus.INTERNAL_SERVER_ERROR.value(),"유저네임 수정 실패"),
    DELETE_FAIL_USERNAME(false,HttpStatus.INTERNAL_SERVER_ERROR.value(),"유저 삭제 실패"),
    MODIFY_FAIL_MEMO(false,HttpStatus.INTERNAL_SERVER_ERROR.value(),"메모 수정 실패"),
    FAILED_PREORDER(false, HttpStatus.INTERNAL_SERVER_ERROR.value(), "사전 결제 정보 등록에 실패했습니다."),
    INVALID_ORDER(false, HttpStatus.INTERNAL_SERVER_ERROR.value(), "결제 정보가 올바르지 않습니다."),
    FAILED_ORDER(false, HttpStatus.INTERNAL_SERVER_ERROR.value(), "결제가 완료되지 못했습니다."),
    UNEXPECTED_ERROR(false, HttpStatus.INTERNAL_SERVER_ERROR.value(), "예상치 못한 에러가 발생했습니다.");


    private final boolean isSuccess;
    private final int code;
    private final String message;

    private BaseResponseStatus(boolean isSuccess, int code, String message) {
        this.isSuccess = isSuccess;
        this.code = code;
        this.message = message;
    }
}
