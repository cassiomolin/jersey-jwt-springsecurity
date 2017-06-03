package com.cassiomolin.example.api.security.service.impl;

import com.cassiomolin.example.api.security.AuthenticatedUserDetails;
import com.cassiomolin.example.api.security.Authority;
import com.cassiomolin.example.persistence.User;
import com.cassiomolin.example.persistence.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Default implementation for the {@link UserDetailsService}.
 *
 * @author cassiomolin
 */
@Service
public class DefaultUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByUsernameOrEmail(username);
        if (user == null) {
            throw new UsernameNotFoundException(String.format("No user found with username '%s'.", username));
        }

        return new AuthenticatedUserDetails.Builder()
                .withUsername(user.getUsername())
                .withPassword(user.getPassword())
                .withAuthorities(mapToGrantedAuthorities(user.getAuthorities()))
                .withActive(user.isActive())
                .build();
    }

    private Set<GrantedAuthority> mapToGrantedAuthorities(Set<Authority> authorities) {
        return authorities.stream()
                .map(authority -> new SimpleGrantedAuthority(authority.toString()))
                .collect(Collectors.toSet());
    }
}