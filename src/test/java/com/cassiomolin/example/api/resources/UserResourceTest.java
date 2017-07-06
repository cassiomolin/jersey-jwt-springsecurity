package com.cassiomolin.example.api.resources;

import com.cassiomolin.example.security.api.resource.AuthenticationResource.AuthenticationToken;
import com.cassiomolin.example.security.api.resource.AuthenticationResource.UserCredentials;
import com.cassiomolin.example.security.domain.Authority;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.List;

import static com.cassiomolin.example.user.api.resource.UserResource.QueryUserResult;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserResourceTest {

    @LocalServerPort
    private int port;

    private URI baseUri;

    private Client client;

    @Before
    public void setUp() throws Exception {
        this.baseUri = new URI("http://localhost:" + port + "/api");
        this.client = ClientBuilder.newClient();
    }

    @Test
    public void getUsersAsAnonymous() {

        Response response = client.target(baseUri).path("users").request().get();
        assertEquals(Response.Status.FORBIDDEN.getStatusCode(), response.getStatus());
    }

    @Test
    public void getUsersAsAsUser() {

        String authorizationHeader = composeAuthorizationHeader(getTokenForUser());

        Response response = client.target(baseUri).path("users").request()
                .header(HttpHeaders.AUTHORIZATION, authorizationHeader).get();
        assertEquals(Response.Status.FORBIDDEN.getStatusCode(), response.getStatus());
    }

    @Test
    public void getUsersAsAsAdmin() {

        String authorizationHeader = composeAuthorizationHeader(getTokenForAdmin());

        Response response = client.target(baseUri).path("users").request()
                .header(HttpHeaders.AUTHORIZATION, authorizationHeader).get();
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        List<QueryUserResult> queryDetailsList = response.readEntity(new GenericType<List<QueryUserResult>>() {
        });
        assertNotNull(queryDetailsList);
        assertThat(queryDetailsList, hasSize(3));
    }

    @Test
    public void getUserAsAnonymous() {

        Long userId = 1L;

        Response response = client.target(baseUri).path("users").path(userId.toString()).request().get();
        assertEquals(Response.Status.FORBIDDEN.getStatusCode(), response.getStatus());
    }

    @Test
    public void getUserAsAsUser() {

        Long userId = 1L;

        String authorizationHeader = composeAuthorizationHeader(getTokenForUser());

        Response response = client.target(baseUri).path("users").path(userId.toString()).request()
                .header(HttpHeaders.AUTHORIZATION, authorizationHeader).get();
        assertEquals(Response.Status.FORBIDDEN.getStatusCode(), response.getStatus());
    }

    @Test
    public void getUserAsAsAdmin() {

        Long userId = 1L;

        String authorizationHeader = composeAuthorizationHeader(getTokenForAdmin());

        Response response = client.target(baseUri).path("users").path(userId.toString()).request()
                .header(HttpHeaders.AUTHORIZATION, authorizationHeader).get();
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        QueryUserResult user = response.readEntity(QueryUserResult.class);
        assertNotNull(user);
        assertEquals(userId, user.getId());
    }

    @Test
    public void getAuthenticatedUserAsAnonymous() {

        Response response = client.target(baseUri).path("users").path("me").request().get();
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        QueryUserResult user = response.readEntity(QueryUserResult.class);
        assertNull(user.getId());
        assertEquals("anonymousUser", user.getUsername());
        assertThat(user.getAuthorities(), is(empty()));
    }

    @Test
    public void getAuthenticatedUserAsAsUser() {

        String authorizationHeader = composeAuthorizationHeader(getTokenForUser());

        Response response = client.target(baseUri).path("users").path("me").request()
                .header(HttpHeaders.AUTHORIZATION, authorizationHeader).get();
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        QueryUserResult user = response.readEntity(QueryUserResult.class);
        assertNotNull(user.getId());
        assertEquals("user", user.getUsername());
        assertThat(user.getAuthorities(), containsInAnyOrder(Authority.USER));
    }

    @Test
    public void getAuthenticatedUserAsAsAdmin() {

        String authorizationHeader = composeAuthorizationHeader(getTokenForAdmin());

        Response response = client.target(baseUri).path("users").path("me").request()
                .header(HttpHeaders.AUTHORIZATION, authorizationHeader).get();
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        QueryUserResult user = response.readEntity(QueryUserResult.class);
        assertNotNull(user.getId());
        assertEquals("admin", user.getUsername());
        assertThat(user.getAuthorities(), containsInAnyOrder(Authority.USER, Authority.ADMIN));
    }

    private String getTokenForUser() {

        UserCredentials credentials = new UserCredentials();
        credentials.setUsername("user");
        credentials.setPassword("password");

        AuthenticationToken authenticationToken = client.target(baseUri).path("auth").request()
                .post(Entity.entity(credentials, MediaType.APPLICATION_JSON), AuthenticationToken.class);
        return authenticationToken.getToken();
    }

    private String getTokenForAdmin() {

        UserCredentials credentials = new UserCredentials();
        credentials.setUsername("admin");
        credentials.setPassword("password");

        AuthenticationToken authenticationToken = client.target(baseUri).path("auth").request()
                .post(Entity.entity(credentials, MediaType.APPLICATION_JSON), AuthenticationToken.class);
        return authenticationToken.getToken();
    }

    public String composeAuthorizationHeader(String authenticationToken) {
        return "Bearer" + " " + authenticationToken;
    }
}
