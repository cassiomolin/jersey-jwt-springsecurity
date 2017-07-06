package com.cassiomolin.example;

import com.cassiomolin.example.security.api.resource.AuthenticationResource;
import org.junit.Before;
import org.springframework.boot.context.embedded.LocalServerPort;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import java.net.URI;

/**
 * Base class for API testing.
 *
 * @author cassiomolin
 */
public class ApiBaseTest {

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

        AuthenticationResource.UserCredentials credentials = new AuthenticationResource.UserCredentials();
        credentials.setUsername("user");
        credentials.setPassword("password");

        AuthenticationResource.AuthenticationToken authenticationToken = client.target(baseUri).path("auth").request()
                .post(Entity.entity(credentials, MediaType.APPLICATION_JSON), AuthenticationResource.AuthenticationToken.class);
        return authenticationToken.getToken();
    }

    protected String getTokenForAdmin() {

        AuthenticationResource.UserCredentials credentials = new AuthenticationResource.UserCredentials();
        credentials.setUsername("admin");
        credentials.setPassword("password");

        AuthenticationResource.AuthenticationToken authenticationToken = client.target(baseUri).path("auth").request()
                .post(Entity.entity(credentials, MediaType.APPLICATION_JSON), AuthenticationResource.AuthenticationToken.class);
        return authenticationToken.getToken();
    }

    protected String composeAuthorizationHeader(String authenticationToken) {
        return "Bearer" + " " + authenticationToken;
    }
}
