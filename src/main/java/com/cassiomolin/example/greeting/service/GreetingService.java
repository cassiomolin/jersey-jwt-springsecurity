package com.cassiomolin.example.greeting.service;

import org.springframework.stereotype.Service;

/**
 * Service for greetings.
 *
 * @author cassiomolin
 */
@Service
public class GreetingService {

    /**
     * Get a public greeting.
     *
     * @return
     */
    public String getPublicGreeting() {
        return "Hello from the other side!";
    }

    /**
     * Get a greeting for a user.
     *
     * @return
     */
    public String getGreetingForUser(String username) {
        return String.format("Hello %s!", username);
    }
}