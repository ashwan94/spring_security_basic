package org.example.security_test.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LoginTryDTO {
    private final String ip;
    private int tryCount;
    private LocalDateTime lastTryTime;

    // 시도된 ip 의 초깃값 설정
    public LoginTryDTO(String ip) {
        this.ip = ip;
        this.tryCount = 1;
        this.lastTryTime = LocalDateTime.now();
    }

    // 시도 횟수 증가
    // 최근 시도일자 업데이트
    public void upCountTry(){
        tryCount++;
        lastTryTime = LocalDateTime.now();
    }

}
