package org.example.security_test.dto;


import org.example.security_test.entity.UserEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

public class CustomUserDetails implements UserDetails{

    private UserEntity userEntity;

    public CustomUserDetails(UserEntity userEntity){
        this.userEntity = userEntity;
    }

    // ROLE 을 return 하는 method
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new ArrayList<>();

        collection.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return userEntity.getRole();
            }
        });
        return collection;
    }

    // PW
    @Override
    public String getPassword() {
        return userEntity.getPassword();
    }

    // ID
    @Override
    public String getUsername() {
        return userEntity.getUsername();
    }

    // 사용자 ID 만료 여부
    // true 일때 만료되지 않음, false 일때 만료됨
    // DB 에 만료여부에 대한 field 를 생성해 verify 가능
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // 사용자 ID 가 LOCK 인지 아닌지
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
