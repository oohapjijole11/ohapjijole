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
    _UNAUTHORIZED_INVALID_TOKEN(HttpStatus.UNAUTHORIZED,"유효하지 않는 JWT 서명입니다."),
    _UNAUTHORIZED_EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED,"만료된 JWT 토큰입니다."),
    _NOT_FOUND_TOKEN(HttpStatus.NOT_FOUND,  "JWT 토큰이 필요합니다."),
    _NOT_PERMITTED_USER(HttpStatus.BAD_REQUEST, "허용되지 않는 유저 입니다"),
    //Auth,USer관련 코드
    _NOT_FOUND_USER(HttpStatus.BAD_REQUEST,"유저를 찾을 수 없습니다"),
    _DELETED_USER(HttpStatus.BAD_REQUEST,"탈퇴한 계정입니다."),
    _PASSWORD_NOT_MATCHES(HttpStatus.UNAUTHORIZED,"비밀번호가 틀렸습니다."),
    _DUPLICATED_EMAIL(HttpStatus.BAD_REQUEST,"중복된 이메일입니다."),
    _INVALID_PASSWORD_FORM(HttpStatus.BAD_REQUEST,"비밀번호는 최소 8자 이상이어야 하며, 대소문자 포함 영문, 숫자, 특수문자를 최소 1글자씩 포함해야 합니다."),
    _BAD_REQUEST_NOT_FOUND_USER(HttpStatus.BAD_REQUEST,"해당 유저를 찾을 수 없습니다."),
    _USER_ROLE_IS_NULL(HttpStatus.BAD_REQUEST,"유저 권한이 없습니다."),
    _USER_NOT_FOUND(HttpStatus.NOT_FOUND,"해당 유저를 찾을 수 없습니다." ),

    //item
    _NOT_FOUND_ITEM(HttpStatus.NOT_FOUND,"상품이 존재하지 않습니다."),

    //attachments
    _ATTACHMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "첨부파일을 찾을 수 없습니다."),

    //auction
    _NOT_FOUND_AUCTION(HttpStatus.NOT_FOUND, "경매가 존재하지 않습니다"),
    _NOT_ALLOWED_TO_UPDATE(HttpStatus.NOT_MODIFIED,"진행중인 경매는 수정할 수 없습니다 "),
    _NOT_FAILED_AUCTION(HttpStatus.NOT_FOUND, "유찰된 경매만 다시 생성 가능합니다"),

    //bid
    _NOT_LARGER_PRICE(HttpStatus.BAD_REQUEST, "입찰가는 현재 최고 입찰가보다 커야합니다."),
    _BID_STATUS_END(HttpStatus.BAD_REQUEST, "이미 끝난 경매입니다."),
    _BID_STATUS_BEFORE(HttpStatus.BAD_REQUEST, "아직 시작하지 않은 경매입니다."),
    _BID_NOT_GOING(HttpStatus.BAD_REQUEST, "경매가 진행되고 있지 않습니다."),
    _SSE_NOT_CONNECT(HttpStatus.INTERNAL_SERVER_ERROR, "경매장과 연결되지 않습니다."),
    _NOT_HAVE_TICKET(HttpStatus.BAD_REQUEST,"경매 티켓을 가지고 있지 않습니다."),
    _INVALID_STATUS(HttpStatus.BAD_REQUEST, "경매 상태값이 유효하지 않습니다."),

    //sbid
    _NOT_AVAILABLE_SLACK_NOTIFICATION(HttpStatus.BAD_REQUEST, "슬랙 알림을 보낼 수 없습니다."),

    //ticket
    _NOT_FIND_TICKET(HttpStatus.BAD_REQUEST,"존재하지 않는 티켓입니다."),

    //S3
    FILE_UPLOAD_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "파일 업로드 에러입니다."),

    // lock
    _LOCK_NOT_AVAILABLE_ERROR(HttpStatus.NOT_FOUND, "락을 이용할 수 없습니다"),
    _LOCK_INTERRUPTED_ERROR(HttpStatus.BAD_REQUEST, "락이 중단 되었습니다");





    private final HttpStatus status;
    private final String message;

    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

}
