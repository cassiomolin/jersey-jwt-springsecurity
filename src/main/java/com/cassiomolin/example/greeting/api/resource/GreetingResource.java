package com.cassiomolin.example.greeting.api.resource;

import com.cassiomolin.example.greeting.service.GreetingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * JAX-RS resource class that provides operations for greetings.
 *
 * @author cassiomolin
 */
@Component
@Path("greetings")
public class GreetingResource {

    @Autowired
    private GreetingService greetingService;

    /**
     * Get a public greeting.
     *
     * @return
     */
    @GET
    @Path("public")
    @Produces(MediaType.TEXT_PLAIN)
    public Response getPublicGreeting() {
        return Response.ok(greetingService.getPublicGreeting()).build();
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
        return Response.ok(greetingService.getGreetingForUser(username)).build();
    }
}