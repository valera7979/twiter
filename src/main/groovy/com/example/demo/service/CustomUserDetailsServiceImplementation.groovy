package com.example.demo.service

import com.example.demo.entity.User
import com.example.demo.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class CustomUserDetailsServiceImplementation implements UserDetailsService {

    @Autowired
    UserRepository userRepository

    @Override
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username)

        if (user == null || user.isLogin_with_google()) {
            throw new UsernameNotFoundException("Username was not found - " + username)
        }

        List<GrantedAuthority> authorities = new ArrayList<>()

        new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authorities)
    }
}
