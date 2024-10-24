package com.sparta.final_project.domain.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    //user

    //auth
    //jwt token 예외
    _BAD_REQUEST_INVALID_EMAIL(HttpStatus.BAD_REQUEST,"이메일 형식으로 작성해야합니다"),
    _BAD_REQUEST_UNSUPPORTED_TOKEN(HttpStatus.BAD_REQUEST,"지원되지 않는 JWT 토큰입니다."),
    _BAD_REQUEST_ILLEGAL_TOKEN(HttpStatus.BAD_REQUEST,"잘못된 JWT 토큰입니다."),
    _UNAUTHORIZED_INVALID_TOKEN(HttpStatus.UNAUTHORIZED,"유효하지 않는 JWT 서명입니다."),
    _UNAUTHORIZED_EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED,"만료된 JWT 토큰입니다."),
    _UNAUTHORIZED_TOKEN(HttpStatus.UNAUTHORIZED,"JWT 토큰 검증 중 오류가 발생했습니다."),
    _FORBIDDEN_TOKEN(HttpStatus.FORBIDDEN,  "관리자 권한이 없습니다."),
    _NOT_FOUND_TOKEN(HttpStatus.NOT_FOUND,  "JWT 토큰이 필요합니다."),
    _NO_MORE_STORE(HttpStatus.BAD_REQUEST,"최대 3개 운영가능"),
    _TEST_ERROR(HttpStatus.BAD_REQUEST,  "ApiException 예외 처리 테스트"),
    _NOT_PERMITTED_USER(HttpStatus.BAD_REQUEST, "허용되지 않는 유저 입니다"),
    //Auth,USer관련 코드
    _NOT_FOUND_USER(HttpStatus.BAD_REQUEST,"유저를 찾을 수 없습니다"),
    _USERNAME_IS_SAME(HttpStatus.BAD_REQUEST,"변경하려는 이름이 전과 동일합니다"),
    _NOT_FOUND_EMAIL(HttpStatus.BAD_REQUEST,"이메일을 찾을 수 없습니다."),
    _DELETED_USER(HttpStatus.BAD_REQUEST,"탈퇴한 계정입니다."),
    _PASSWORD_NOT_MATCHES(HttpStatus.UNAUTHORIZED,"비밀번호가 틀렸습니다."),
    _DUPLICATED_EMAIL(HttpStatus.BAD_REQUEST,"중복된 이메일입니다."),
    _INVALID_EMAIL_FORM(HttpStatus.BAD_REQUEST,"이메일 형식이 올바르지 않습니다."),
    _INVALID_PASSWORD_FORM(HttpStatus.BAD_REQUEST,"비밀번호는 최소 8자 이상이어야 하며, 대소문자 포함 영문, 숫자, 특수문자를 최소 1글자씩 포함해야 합니다."),
    _INVALID_USER_INFO(HttpStatus.BAD_REQUEST,"변경하려는 정보가 잘못되었습니다."),
    _INVALID_BIRTHDAY(HttpStatus.BAD_REQUEST,"잘못된 생일 값입니다"),
    _BAD_REQUEST_NOT_FOUND_USER(HttpStatus.BAD_REQUEST,"해당 유저를 찾을 수 없습니다."),
    _PASSWORD_IS_DUPLICATED(HttpStatus.BAD_REQUEST,"이미 사용중인 비밀번호로 변경할 수 없습니다."),
    _INVALID_USER_ROLE(HttpStatus.BAD_REQUEST,"잘못된 유저권한 입니다."),
    _USER_ROLE_IS_NULL(HttpStatus.BAD_REQUEST,"유저 권한이 없습니다."),
    _INVALID_USER_NAME(HttpStatus.BAD_REQUEST,"유저이름은 최소 3자 이상,20자 이하여야 하며, 대소문자 포함 영문,숫자만 사용가능합니다." ),
    _AUTH_DELETED_USER(HttpStatus.FORBIDDEN,  "로그인 이메일과 입력한 이메일이 일치하지 않습니다."),
    _USER_NOT_FOUND(HttpStatus.NOT_FOUND,"해당 유저를 찾을 수 없습니다." ),

    //item
    _NOT_FOUND_ITEM(HttpStatus.NOT_FOUND,"상품이 존재하지 않습니다."),

    //auction
    _NOT_FOUND_AUCTION(HttpStatus.NOT_FOUND, "경매가 존재하지 않습니다"),

    //bid
    _NOT_LARGER_PRICE(HttpStatus.BAD_REQUEST, "입찰가는 현재 최고 입찰가보다 커야합니다."),
    _BID_STATUS_END(HttpStatus.BAD_REQUEST, "이미 끝난 경매입니다."),

    //sbid

    //ticket

    // ### 아래 코드 위에 ErrorCode 작성해 주세요! ErrorCode 메서드 사이는 ,(컴마)로 구분해 주세요! ###
    _NOT_FOUND(HttpStatus.NOT_FOUND, "찾지못했습니다.");





    private final HttpStatus status;
    private final String message;

    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}
