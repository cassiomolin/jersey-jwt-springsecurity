package com.cassiomolin.example.security.api.resource;

import com.cassiomolin.example.security.api.resource.AuthenticationResource.AuthenticationToken;
import com.cassiomolin.example.security.api.resource.AuthenticationResource.UserCredentials;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthenticationResourceTest {

    @LocalServerPort
    private int port;

    private URI uri;

    private Client client;

    @Before
    public void setUp() throws Exception {
        this.uri = new URI("http://localhost:" + port + "/api");
        this.client = ClientBuilder.newClient();
    }

    @Test
    public void authenticateWithValidCredentials() {

        UserCredentials credentials = new UserCredentials();
        credentials.setUsername("admin");
        credentials.setPassword("password");

        Response response = client.target(uri).path("auth").request()
                .post(Entity.entity(credentials, MediaType.APPLICATION_JSON));
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        AuthenticationToken authenticationToken = response.readEntity(AuthenticationToken.class);
        assertNotNull(authenticationToken);
        assertNotNull(authenticationToken.getToken());
    }

    @Test
    public void authenticateWithInvalidCredentials() {

        UserCredentials credentials = new UserCredentials();
        credentials.setUsername("invalid-user");
        credentials.setPassword("wrong-password");

        Response response = client.target(uri).path("auth").request()
                .post(Entity.entity(credentials, MediaType.APPLICATION_JSON));
        assertEquals(Response.Status.FORBIDDEN.getStatusCode(), response.getStatus());
    }
}
