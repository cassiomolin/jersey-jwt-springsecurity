package com.cassiomolin.example.api.resources;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Resource class for greeting-related operations.
 *
 * @author cassiomolin
 */
@Component
@Path("greetings")
public class GreetingResource {

    /**
     * Get a public greeting.
     *
     * @return
     */
    @GET
    @Path("public")
    @Produces(MediaType.TEXT_PLAIN)
    public Response getPublicGreeting() {
        return Response.ok("Hello from the other side!").build();
    }

    /**
     * Get a greeting for authenticated users.
     *
     * @return
     */
    @GET
    @Path("protected")
    @Produces(MediaType.TEXT_PLAIN)
    @PreAuthorize("hasAuthority('USER')")
    public Response getProtectedGreeting() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return Response.ok(String.format("Hello %s!", username)).build();
    }
}