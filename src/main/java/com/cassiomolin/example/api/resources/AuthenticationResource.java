package com.cassiomolin.example.api.resources;

import com.cassiomolin.example.api.security.AuthenticationTokenDetails;
import com.cassiomolin.example.api.security.Authority;
import com.cassiomolin.example.api.security.service.AuthenticationTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Resource class for authentication-related operations.
 *
 * @author cassiomolin
 */
@Component
@Path("auth")
public class AuthenticationResource {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private AuthenticationTokenService authenticationTokenService;

    /**
     * Validate user credentials and issue a token for the user.
     *
     * @param credentials
     * @return
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response authenticate(UserCredentials credentials) {

        Authentication authenticationRequest =
                new UsernamePasswordAuthenticationToken(credentials.getUsername(), credentials.getPassword());
        Authentication authenticationResult = authenticationManager.authenticate(authenticationRequest);
        SecurityContextHolder.getContext().setAuthentication(authenticationResult);

        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        Set<Authority> authorities = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream()
                .map(grantedAuthority -> Authority.valueOf(grantedAuthority.toString()))
                .collect(Collectors.toSet());

        String token = authenticationTokenService.issueToken(username, authorities);
        AuthenticationToken authenticationToken = new AuthenticationToken();
        authenticationToken.setToken(token);

        return Response.ok(authenticationToken).build();
    }

    /**
     * Refresh the authentication token for the current user.
     *
     * @return
     */
    @POST
    @Path("refresh")
    @Produces(MediaType.APPLICATION_JSON)
    public Response refresh() {

        AuthenticationTokenDetails tokenDetails = (AuthenticationTokenDetails)
                SecurityContextHolder.getContext().getAuthentication().getDetails();

        String token = authenticationTokenService.refreshToken(tokenDetails);
        AuthenticationToken authenticationToken = new AuthenticationToken();
        authenticationToken.setToken(token);

        return Response.ok(authenticationToken).build();
    }

    /**
     * API model for the authentication token.
     */
    public static class AuthenticationToken {

        private String token;

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }

    /**
     * API model for the user credentials.
     */
    public static class UserCredentials {

        private String username;
        private String password;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
}
