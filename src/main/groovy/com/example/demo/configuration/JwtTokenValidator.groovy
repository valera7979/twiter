package com.example.demo.configuration

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.apache.catalina.authenticator.SpnegoAuthenticator
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter

import javax.crypto.SecretKey
import io.jsonwebtoken.security.Keys

import java.security.Key

class JwtTokenValidator extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        var jwt = request.getHeader(JwtConstant.JWT_HEADER)

        if (jwt != null) {
            jwt = jwt.substring("Bearer".length() + 1)

            try {
                SecretKey key = Keys.hmacShaKeyFor(JwtConstant.SECRET_KEY.getBytes())
                Claims claims = Jwts.parser().setSigningKey(key).build().parseClaimsJws(jwt).getBody()

                var email = String.valueOf(claims.get("email"))
                var authorities = String.valueOf(claims.get("authorities"))

                var grantedAuthorities = AuthorityUtils.commaSeparatedStringToAuthorityList(authorities)
                Authentication authentication = new UsernamePasswordAuthenticationToken(email, null, grantedAuthorities)

                SecurityContextHolder.getContext().setAuthentication(authentication)

            } catch(Exception e) {
                throw new BadCredentialsException("Invalid token!")
            }

        }

        filterChain.doFilter(request, response)

    }
}

