package com.sparta.final_project.config.exception;

public class DistributedLockKeyGeneratorException extends RuntimeException{

    // 기본 생성자
    public DistributedLockKeyGeneratorException() {
        super("분산 잠금 키를 생성하지 못했습니다.");
    }

    // 메시지를 지정할 수 있는 생성자
    public DistributedLockKeyGeneratorException(String message) {
        super(message);
    }

    // 원인을 포함할 수 있는 생성자
    public DistributedLockKeyGeneratorException(String message, Throwable cause) {
        super(message, cause);
    }

    // 원인만으로 생성하는 생성자
    public DistributedLockKeyGeneratorException(Throwable cause) {
        super(cause);
    }


}
