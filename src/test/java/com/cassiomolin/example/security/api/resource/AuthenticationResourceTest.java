package com.cassiomolin.example.security.api.resource;

import com.cassiomolin.example.ApiBaseTest;
import com.cassiomolin.example.security.api.model.AuthenticationToken;
import com.cassiomolin.example.security.api.model.UserCredentials;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Tests for the authentication resource class.
 *
 * @author cassiomolin
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthenticationResourceTest extends ApiBaseTest {

    @Test
    public void authenticateWithValidCredentials() {

        UserCredentials credentials = new UserCredentials();
        credentials.setUsername("admin");
        credentials.setPassword("password");

        Response response = client.target(baseUri).path("auth").request()
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

        Response response = client.target(baseUri).path("auth").request()
                .post(Entity.entity(credentials, MediaType.APPLICATION_JSON));
        assertEquals(Response.Status.FORBIDDEN.getStatusCode(), response.getStatus());
    }
}
