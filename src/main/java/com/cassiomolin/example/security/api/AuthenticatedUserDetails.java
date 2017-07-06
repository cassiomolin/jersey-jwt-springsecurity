package com.cassiomolin.example.security.api;

import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

/**
 * Default implementation for the {@link UserDetails} interface.
 *
 * @author cassiomolin
 */
public final class AuthenticatedUserDetails implements UserDetails, CredentialsContainer {

    private final String username;
    private String password;
    private final Set<GrantedAuthority> authorities;
    private final boolean active;

    private AuthenticatedUserDetails(String username, String password, Set<GrantedAuthority> authorities, boolean active) {
        this.username = username;
        this.password = password;
        this.authorities = Collections.unmodifiableSet(authorities);
        this.active = active;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return active;
    }

    @Override
    public void eraseCredentials() {
        this.password = null;
    }

    /**
     * Builder for the {@link AuthenticatedUserDetails} class.
     */
    public static class Builder {

        private String username;
        private String password;
        private Set<GrantedAuthority> authorities;
        private boolean active;

        public Builder withUsername(String username) {
            this.username = username;
            return this;
        }

        public Builder withPassword(String password) {
            this.password = password;
            return this;
        }

        public Builder withAuthorities(Set<GrantedAuthority> authorities) {
            this.authorities = authorities;
            return this;
        }

        public Builder withActive(boolean active) {
            this.active = active;
            return this;
        }

        public AuthenticatedUserDetails build() {
            return new AuthenticatedUserDetails(username, password, authorities, active);
        }
    }
}
