package com.cassiomolin.example.greeting.api.resource;

import com.cassiomolin.example.ApiBaseTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

import static org.junit.Assert.assertEquals;

/**
 * Tests for the greeting resource class.
 *
 * @author cassiomolin
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GreetingResourceTest extends ApiBaseTest {

    @Test
    public void getPublicGreetingAsAnonymous() {
        Response response = client.target(baseUri).path("greetings").path("public").request().get();
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    public void getProtectedGreetingAsAnonymous() {
        Response response = client.target(baseUri).path("greetings").path("protected").request().get();
        assertEquals(Response.Status.FORBIDDEN.getStatusCode(), response.getStatus());
    }

    @Test
    public void getPublicGreetingAsAdmin() {

        String authorizationHeader = composeAuthorizationHeader(getTokenForAdmin());

        Response response = client.target(baseUri).path("greetings").path("public").request()
                .header(HttpHeaders.AUTHORIZATION, authorizationHeader).get();
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }

    @Test
    public void getProtectedGreetingAsAdmin() {

        String authorizationHeader = composeAuthorizationHeader(getTokenForAdmin());

        Response response = client.target(baseUri).path("greetings").path("protected").request()
                .header(HttpHeaders.AUTHORIZATION, authorizationHeader).get();
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
    }
}
