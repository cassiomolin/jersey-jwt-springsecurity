package com.cassiomolin.example.user.api.resource;

import com.cassiomolin.example.ApiBaseTest;
import com.cassiomolin.example.security.domain.Authority;
import com.cassiomolin.example.user.api.model.QueryUserResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

/**
 * Tests for the user resource class.
 *
 * @author cassiomolin
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserResourceTest extends ApiBaseTest {

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

        List<QueryUserResult> queryResults = response.readEntity(new GenericType<List<QueryUserResult>>() {});
        assertNotNull(queryResults);
        assertThat(queryResults, hasSize(3));
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

        QueryUserResult queryResults = response.readEntity(QueryUserResult.class);
        assertNotNull(queryResults);
        assertEquals(userId, queryResults.getId());
    }

    @Test
    public void getAuthenticatedUserAsAnonymous() {

        Response response = client.target(baseUri).path("users").path("me").request().get();
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        QueryUserResult queryResults = response.readEntity(QueryUserResult.class);
        assertNull(queryResults.getId());
        assertEquals("anonymousUser", queryResults.getUsername());
        assertThat(queryResults.getAuthorities(), is(empty()));
    }

    @Test
    public void getAuthenticatedUserAsAsUser() {

        String authorizationHeader = composeAuthorizationHeader(getTokenForUser());

        Response response = client.target(baseUri).path("users").path("me").request()
                .header(HttpHeaders.AUTHORIZATION, authorizationHeader).get();
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        QueryUserResult queryResults = response.readEntity(QueryUserResult.class);
        assertNotNull(queryResults.getId());
        assertEquals("user", queryResults.getUsername());
        assertThat(queryResults.getAuthorities(), containsInAnyOrder(Authority.USER));
    }

    @Test
    public void getAuthenticatedUserAsAsAdmin() {

        String authorizationHeader = composeAuthorizationHeader(getTokenForAdmin());

        Response response = client.target(baseUri).path("users").path("me").request()
                .header(HttpHeaders.AUTHORIZATION, authorizationHeader).get();
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        QueryUserResult queryResults = response.readEntity(QueryUserResult.class);
        assertNotNull(queryResults.getId());
        assertEquals("admin", queryResults.getUsername());
        assertThat(queryResults.getAuthorities(), containsInAnyOrder(Authority.USER, Authority.ADMIN));
    }
}
