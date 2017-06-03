package com.cassiomolin.example.api;

import com.cassiomolin.example.api.providers.ObjectMapperProvider;
import com.cassiomolin.example.api.resources.AuthenticationResource;
import com.cassiomolin.example.api.resources.GreetingResource;
import com.cassiomolin.example.api.resources.UserResource;
import com.cassiomolin.example.api.security.exceptions.mappers.AccessDeniedExceptionMapper;
import com.cassiomolin.example.api.security.exceptions.mappers.AuthenticationExceptionMapper;
import com.cassiomolin.example.api.security.exceptions.mappers.AuthenticationTokenRefreshmentExceptionMapper;
import org.glassfish.jersey.server.ResourceConfig;
import org.springframework.stereotype.Component;

import javax.ws.rs.ApplicationPath;

/**
 * Jersey configuration class.
 *
 * @author cassiomolin
 */
@Component
@ApplicationPath("api")
public class JerseyConfig extends ResourceConfig {

    public JerseyConfig() {

        register(AuthenticationResource.class);
        register(GreetingResource.class);
        register(UserResource.class);

        register(AccessDeniedExceptionMapper.class);
        register(AuthenticationExceptionMapper.class);
        register(AuthenticationTokenRefreshmentExceptionMapper.class);

        register(ObjectMapperProvider.class);
    }
}