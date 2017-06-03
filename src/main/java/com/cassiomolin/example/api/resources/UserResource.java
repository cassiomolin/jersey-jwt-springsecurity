package com.cassiomolin.example.api.resources;

import com.cassiomolin.example.api.security.Authority;
import com.cassiomolin.example.persistence.User;
import com.cassiomolin.example.persistence.repository.UserRepository;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Resource class for user-related operations.
 *
 * @author cassiomolin
 */
@Component
@Path("users")
public class UserResource {

    @Context
    private UriInfo uriInfo;

    @Autowired
    private UserRepository userRepository;

    /**
     * Get all users.
     *
     * @return
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @PreAuthorize("hasAuthority('ADMIN')")
    public Response getUsers() {

        Iterable<User> iterable = userRepository.findAll();
        List<UserQueryDetails> queryDetailsList =
                StreamSupport.stream(iterable.spliterator(), false)
                        .map(this::toUserQueryDetails)
                        .collect(Collectors.toList());

        return Response.ok(queryDetailsList).build();
    }

    /**
     * Get a user with the given identifier.
     *
     * @param userId
     * @return
     */
    @GET
    @Path("{userId}")
    @Produces(MediaType.APPLICATION_JSON)
    @PreAuthorize("hasAuthority('ADMIN')")
    public Response getUser(@PathParam("userId") Long userId) {

        User user = userRepository.findOne(userId);
        if (user == null) {
            throw new NotFoundException();
        }

        UserQueryDetails queryDetails = toUserQueryDetails(user);
        return Response.ok(queryDetails).build();
    }

    /**
     * Get details from the current user.
     *
     * @return
     */
    @GET
    @Path("me")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAuthenticatedUser() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication instanceof AnonymousAuthenticationToken) {
            UserQueryDetails queryDetails = new UserQueryDetails();
            queryDetails.setUsername(authentication.getName());
            queryDetails.setAuthorities(new HashSet<>());
            return Response.ok(queryDetails).build();
        }

        User user = userRepository.findByUsernameOrEmail(authentication.getName());
        UserQueryDetails queryDetails = toUserQueryDetails(user);
        return Response.ok(queryDetails).build();
    }

    /**
     * Map a {@link User} instance to a {@link UserQueryDetails} instance.
     *
     * @param user
     * @return
     */
    private UserQueryDetails toUserQueryDetails(User user) {
        UserQueryDetails queryDetails = new UserQueryDetails();
        queryDetails.setId(user.getId());
        queryDetails.setFirstName(user.getFirstName());
        queryDetails.setLastName(user.getLastName());
        queryDetails.setEmail(user.getEmail());
        queryDetails.setUsername(user.getUsername());
        queryDetails.setAuthorities(user.getAuthorities());
        queryDetails.setActive(user.isActive());
        return queryDetails;
    }

    /**
     * API model for returning user details.
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class UserQueryDetails {

        private Long id;
        private String firstName;
        private String lastName;
        private String email;
        private String username;
        private Set<Authority> authorities;
        private Boolean active;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public Set<Authority> getAuthorities() {
            return authorities;
        }

        public void setAuthorities(Set<Authority> authorities) {
            this.authorities = authorities;
        }

        public Boolean getActive() {
            return active;
        }

        public void setActive(Boolean active) {
            this.active = active;
        }
    }
}
