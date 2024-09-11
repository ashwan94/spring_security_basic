package org.example.security_test.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.security_test.dto.JoinDTO;
import org.example.security_test.entity.UserEntity;
import org.example.security_test.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class JoinService {

    private UserRepo userRepo;

    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public void joinProcess(JoinDTO joinDTO){

        // DB 에 이미 동일한 username 을 가진 회원이 존재하는지 검증(verify)
        boolean isUser = userRepo.existsByUsername(joinDTO.getUsername());

        // DB 에 동일한 유저가 있으면 method 종료
        if(isUser){
            return;
        }

        UserEntity data = new UserEntity();

        data.setUsername(joinDTO.getUsername());
        data.setPassword(bCryptPasswordEncoder.encode(joinDTO.getPassword()));  // 필수적으로 암호화(HASH)시켜야함
        data.setRole("ROLE_ADMIN");

        userRepo.save(data);
    }

}
