package com.example.todo.auth;

import com.example.todo.userapi.entity.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
// 역할: 토큰을 발급하고, 서명 위조를 검사하는 객체.
public class TokenProvider {

    //서명에 사용할 값 (512비트 이상의 랜덤 문자열)
    @Value("${jwt.secret}")
    private String SECRET_KEY;

    //토큰 생성 메서드
    /**
    * JSON Web Token을 생성하는 메서드
    * @param userEntity -토큰의 내용(클레임)에 포함될 유저 정보
    * @return - 생성된 JSON을 암호화한 토큰값.
    */
    public String createToken(User userEntity) {

        //토큰 생성
        /*
            {

            }
        *
         */
        return Jwts.builder()
                //token header에 들어갈 서명
                .signWith(
                        Keys.hmacShaKeyFor(SECRET_KEY.getBytes()),
                        SignatureAlgorithm.ES512
                )
                .compact();

        //token payload에 들어갈 클레임 설정




    }

}
