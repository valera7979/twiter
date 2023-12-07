package com.example.demo.configuration

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.AuthorityUtils
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter

import javax.crypto.SecretKey

class JwtTokenValidator extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        var jwt = request.getHeader(JwtConstant.JWT_HEADER)

        if (jwt != null) {
            jwt = jwt.substring("Bearer".length() + 1)

            try {
                SecretKey key = Keys.hmacShaKeyFor(JwtConstant.SECRET_KEY.getBytes())
                Claims claims = Jwts.parser().verifyWith(key).build().parseSignedClaims(jwt).getPayload()

                var email = String.valueOf(claims.get("email"))
                var authorities = String.valueOf(claims.get("authorities"))

                var grantedAuthorities = AuthorityUtils.commaSeparatedStringToAuthorityList(authorities)
                Authentication authentication = new UsernamePasswordAuthenticationToken(email, null, grantedAuthorities)

                SecurityContextHolder.getContext().setAuthentication(authentication)

            } catch(Exception e) {
                throw new BadCredentialsException("Invalid token!", e)
            }

        }

        filterChain.doFilter(request, response)

    }
}

