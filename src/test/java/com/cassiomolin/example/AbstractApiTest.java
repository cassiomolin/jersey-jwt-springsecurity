package com.cassiomolin.example;

import com.cassiomolin.example.security.api.model.AuthenticationToken;
import com.cassiomolin.example.security.api.model.UserCredentials;
import com.cassiomolin.example.security.api.resource.AuthenticationResource;
import org.junit.Before;
import org.springframework.boot.context.embedded.LocalServerPort;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import java.net.URI;

/**
 * Base class for REST API testing.
 *
 * @author cassiomolin
 */
public abstract class AbstractApiTest {

    @LocalServerPort
    protected int port;

    protected URI baseUri;

    protected Client client;

    @Before
    public void setUp() throws Exception {
        this.baseUri = new URI("http://localhost:" + port + "/api");
        this.client = ClientBuilder.newClient();
    }

    protected String getTokenForUser() {

        UserCredentials credentials = new UserCredentials();
        credentials.setUsername("user");
        credentials.setPassword("password");

        AuthenticationToken authenticationToken = client.target(baseUri).path("auth").request()
                .post(Entity.entity(credentials, MediaType.APPLICATION_JSON), AuthenticationToken.class);
        return authenticationToken.getToken();
    }

    protected String getTokenForAdmin() {

        UserCredentials credentials = new UserCredentials();
        credentials.setUsername("admin");
        credentials.setPassword("password");

        AuthenticationToken authenticationToken = client.target(baseUri).path("auth").request()
                .post(Entity.entity(credentials, MediaType.APPLICATION_JSON), AuthenticationToken.class);
        return authenticationToken.getToken();
    }

    protected String composeAuthorizationHeader(String authenticationToken) {
        return "Bearer" + " " + authenticationToken;
    }
}
