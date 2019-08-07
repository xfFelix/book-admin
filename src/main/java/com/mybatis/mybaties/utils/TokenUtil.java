package com.mybatis.mybaties.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class TokenUtil {

    /**
     * 过期时间
     */
    private static final long EXPIRE_TIME = 15 * 60 * 1000;

    /**
     * token私钥
     */
    private static final String TOKEN_SECRET = "f26e587c28064d0e855e72c0a6a0e618";

    public static String sign(String username, String userId) throws UnsupportedEncodingException {
        Date date = new Date(System.currentTimeMillis() + EXPIRE_TIME);
        Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
        Map< String, Object> header = new HashMap<>(2);
        header.put("typ", "JWT");
        header.put("alg", "HS256");
        return JWT.create()
                .withClaim("userName", username)
                .withClaim("userId", userId)
                .withExpiresAt(date)
                .sign(algorithm);
    }

    public static boolean validate(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(TOKEN_SECRET);
            JWTVerifier verifier = JWT.require(algorithm).build();
            DecodedJWT jwt = verifier.verify(token);
            return true;
        } catch (UnsupportedEncodingException e) {
            return false;
        }
    }

    public static String getUserName(String token) {
        DecodedJWT jwt = JWT.decode(token);
        return jwt.getClaim("userName").asString();
    }

    public static String getUserId(String token) {
        DecodedJWT jwt = JWT.decode(token);
        return jwt.getClaim("userId").asString();
    }
}
