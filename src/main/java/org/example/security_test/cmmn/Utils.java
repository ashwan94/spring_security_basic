package org.example.security_test.cmmn;

import jakarta.servlet.http.HttpServletRequest;

public class Utils {

    public static String getClientIP(HttpServletRequest request){
        String remoteAddr = "";

        if(request != null){
            remoteAddr = request.getHeader("X-Forwarded-For");
            if(remoteAddr == null || remoteAddr.isEmpty()){
                remoteAddr = request.getRemoteAddr();
            }
        }
        return remoteAddr;
    }
}
