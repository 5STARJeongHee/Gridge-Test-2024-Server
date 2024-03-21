package com.example.demo.utils;


import com.example.demo.common.Role;
import com.example.demo.common.config.JwtProperties;
import com.example.demo.common.exceptions.BaseException;
import com.example.demo.src.user.model.SecurityUser;
import io.jsonwebtoken.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.*;

import static com.example.demo.common.response.BaseResponseStatus.EMPTY_JWT;
import static com.example.demo.common.response.BaseResponseStatus.INVALID_JWT;

@RequiredArgsConstructor
@Service
public class JwtService {
    private final static String CLAIMS_USER_ID = "userId";
    private final static String CLAIMS_USER_ROLE = "role";

    private final static String ROLE_PREFIX = "ROLE_";
    private final JwtProperties jwtProperties;

    public String generateToken(SecurityUser user, Duration expiredAt){
        Date now = new Date();
        return createJwt(user, new Date(now.getTime() + expiredAt.toMillis()));
    }

    public String generateAccessToken(SecurityUser user){
        Date now = new Date();
        return createJwt(
                user,
                new Date(now.getTime() + Duration.ofMinutes(jwtProperties.getAccessToken().getExpiredAt()).toMillis()));
    }

    public String generateRefreshToken(SecurityUser user){
        Date now = new Date();
        return createJwt(
                user,
                new Date(now.getTime() + Duration.ofMinutes(jwtProperties.getRefreshToken().getExpiredAt()).toMillis()));
    }
    /*
    JWT 생성
    @param userId
    @return String
     */
    public String createJwt(SecurityUser user, Date expiry){
        Date now = new Date();
        return Jwts.builder()
                .setHeaderParam(Header.TYPE,Header.JWT_TYPE)
                .claim(CLAIMS_USER_ID,user.getUsername())
                .claim(CLAIMS_USER_ROLE, user.getRole().toString())
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(SignatureAlgorithm.HS256, jwtProperties.getSecretKey())
                .compact();
    }

    public boolean validJwt(String token){
        try {
            Jwts.parser()
                    .setSigningKey(jwtProperties.getSecretKey())
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e){
            return false;
        }
    }

    public Authentication getAuthentication(String token) throws BaseException{
        Claims claims = getClaims(token);
        String role = getUserRole(claims);
        SecurityUser user = SecurityUser.builder()
                .email(getUserId(claims))
                .role(Role.valueOf(role)).build();
        return new UsernamePasswordAuthenticationToken(
                user,
                token,
                user.getAuthorities()
        );
    }
    /*
    JWT에서 userId 추출
    @return Long
    @throws BaseException
     */
    public String getUserId(String token) throws BaseException{
        Claims claims = getClaims(token);
        // 3. userIdx 추출
        return claims.get(CLAIMS_USER_ID,String.class);
    }
    public String getUserId(Claims claims) throws BaseException{
        return claims.get(CLAIMS_USER_ID,String.class);
    }
    public String getUserRole(String token) throws BaseException{
        Claims claims = getClaims(token);
        return claims.get(CLAIMS_USER_ROLE, String.class);
    }
    public String getUserRole(Claims claims) {
        return claims.get(CLAIMS_USER_ROLE, String.class);
    }

    private Claims getClaims(String token) throws BaseException{
        if(token == null || token.length() == 0){
            throw new BaseException(EMPTY_JWT);
        }

        // 2. JWT parsing
        Jws<Claims> claims;
        try{
            claims = Jwts.parser()
                    .setSigningKey(jwtProperties.getSecretKey())
                    .parseClaimsJws(token);
        } catch (Exception ignored) {
            throw new BaseException(INVALID_JWT);
        }
        return claims.getBody();
    }

}
