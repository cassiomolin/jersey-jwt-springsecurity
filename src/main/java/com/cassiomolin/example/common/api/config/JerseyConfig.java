package com.cassiomolin.example.common.api.config;

import com.cassiomolin.example.common.api.provider.ObjectMapperProvider;
import com.cassiomolin.example.greeting.api.resource.GreetingResource;
import com.cassiomolin.example.security.api.exceptionmapper.AccessDeniedExceptionMapper;
import com.cassiomolin.example.security.api.exceptionmapper.AuthenticationExceptionMapper;
import com.cassiomolin.example.security.api.exceptionmapper.AuthenticationTokenRefreshmentExceptionMapper;
import com.cassiomolin.example.security.api.resource.AuthenticationResource;
import com.cassiomolin.example.user.api.resource.UserResource;
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