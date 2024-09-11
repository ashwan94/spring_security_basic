package org.example.security_test.service;

import org.example.security_test.dto.LoginTryDTO;
import org.example.security_test.entity.IpBlockEntity;
import org.example.security_test.repo.IpBlockRepo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.StringUtils;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;


@Service
public class LoginTryService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final int COUNT = 3;  // 최대 시도 횟수 3회
    private final long LOCK = 30; // 30초
    private final HashMap<String, LoginTryDTO> tries = new HashMap<>();

    @Autowired
    private IpBlockRepo ipBlockRepo;

    // 로그인 성공 시 기존 시도 횟수 삭제
    public void loginSuccess(String key) {tries.remove(key);}

    // 로그인 실패 시 시도 횟수 증가
    // 시도 횟수 3회 이상 시 IP 차단
    public void loginFailed(String key){
        tries.compute(key, (k, v) -> {
            logger.info("key : ", key);
            logger.info("k : ", k);
            logger.info("v : ", v);
            if(v == null){
                logger.info("null 로 차단된 IP : " + key);
                return new LoginTryDTO(key);  // 최초 실패 시 account, IP 정보 생성
            }else{
                v.upCountTry();
                if(v.getTryCount() >= COUNT){ // 추가 실패 시 COUNT 누적
                    logger.info("3회 초과로 차단된 IP : " + key);

                    IpBlockEntity ipBlockEntity = new IpBlockEntity();
                    LocalDateTime now = LocalDateTime.now();
                    ipBlockEntity.setIp(key);
                    ipBlockEntity.setAccessDate(now.toString());

                    ipBlockRepo.save(ipBlockEntity);
                }
                return v;
            }
        });
    }

    // Client 의 IP 가 block 인지 아닌지 check
    public boolean isBlocked(String key){
        LoginTryDTO goTry = tries.get(key);

        // 로그인 실패 이력이 없거나, 실패 횟수가 COUNT(3회) 이하인 경우 pass
        if(goTry == null || goTry.getTryCount() < COUNT){
            return false;
        }
        return LocalDateTime.now().isBefore(goTry.getLastTryTime().plusSeconds(LOCK));
    }

    // 차단된 IP 에게 n초 후 재시도 하라는 알림
//    public long getSecondsUntilUnlock(String key){
//        LoginTryDTO goTry = tries.get(key);
//        if(goTry == null){
//            return 0;
//        }
//        long secondsPassed = LocalDateTime.now().until(goTry.getLastTryTime().plusSeconds(LOCK), ChronoUnit.SECONDS);
//        return Math.max(secondsPassed, 0);
//    }

    // 1초 단위로 차단된 IP 가 마지막 접속 시도 시간을 기준으로 LOCK(30초) 가 지났을때 block 해제
//    @Scheduled(fixedRate = 1000)
//    public void removeExpiredBlocks(){
//        tries.forEach((key, value) -> {
//            if(LocalDateTime.now().isBefore(value.getLastTryTime().plusSeconds(LOCK))){
//                tries.remove(key);
//                // TODO DB IP 삭제
//                logger.info("IP Unblocked : " + key);
//            }
//        });
//    }
}
