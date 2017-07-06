package com.cassiomolin.example.api.resources;

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
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GreetingResourceTest {

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
    public void getPublicGreetingAsAnonymous() {

        Response response = client.target(uri).path("greetings").path("public").request().get();
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    public void getProtectedGreetingAsAnonymous() {

        Response response = client.target(uri).path("greetings").path("protected").request().get();
        assertEquals(Response.Status.FORBIDDEN.getStatusCode(), response.getStatus());
    }

    @Test
    public void getPublicGreetingAsAdmin() {

        String authorizationHeader = composeAuthorizationHeader(getTokenForAdmin());

        Response response = client.target(uri).path("greetings").path("public").request()
                .header(HttpHeaders.AUTHORIZATION, authorizationHeader).get();
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    public void getProtectedGreetingAsAdmin() {

        String authorizationHeader = composeAuthorizationHeader(getTokenForAdmin());

        Response response = client.target(uri).path("greetings").path("protected").request()
                .header(HttpHeaders.AUTHORIZATION, authorizationHeader).get();
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }


    private String getTokenForAdmin() {

        UserCredentials credentials = new UserCredentials();
        credentials.setUsername("admin");
        credentials.setPassword("password");

        AuthenticationToken authenticationToken = client.target(uri).path("auth").request()
                .post(Entity.entity(credentials, MediaType.APPLICATION_JSON), AuthenticationToken.class);
        return authenticationToken.getToken();
    }

    public String composeAuthorizationHeader(String authenticationToken) {
        return "Bearer" + " " + authenticationToken;
    }
}
