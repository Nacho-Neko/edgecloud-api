package com.mikou.edgecloud.common.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;

/**
 * JWT Token 类型工具类
 */
public class JwtTokenType {

    /**
     * 判断是否为管理员 Token
     */
    public static boolean isAdminToken(Jws<Claims> jws) {
        if (jws == null) {
            return false;
        }
        Claims claims = jws.getPayload();
        String auth = claims.get("auth", String.class);
        return auth != null && auth.startsWith("admin:");
    }

    /**
     * 判断是否为普通账户 Token
     */
    public static boolean isAccountToken(Jws<Claims> jws) {
        if (jws == null) {
            return false;
        }
        Claims claims = jws.getPayload();
        String auth = claims.get("auth", String.class);
        return auth != null && !auth.startsWith("admin:");
    }
}
