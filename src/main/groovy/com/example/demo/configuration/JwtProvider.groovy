package com.example.demo.configuration

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service

import javax.crypto.SecretKey

@Service
class JwtProvider {

    SecretKey key = Keys.hmacShaKeyFor(JwtConstant.SECRET_KEY.getBytes())

    String generateToken(Authentication authentication) {

        Jwts.builder()
        .issuedAt(new Date())
        .expiration(new Date(new Date().getTime() + 10_800_000))
        .claim("email", authentication.getName())
        .signWith(key)
        .compact()
    }

    String getEmailFromToken(String jwt) {
        jwt = jwt.substring("Bearer".length() + 1)
        Claims claims = Jwts.parser().verifyWith(key).build().parseSignedClaims(jwt).getPayload()

        String.valueOf(claims.get("email"))
    }
}
