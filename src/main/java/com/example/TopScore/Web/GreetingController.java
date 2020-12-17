package com.example.TopScore.Web;

import com.example.TopScore.Shared.ApiResponse.test;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicLong;

@RestController
public class GreetingController {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @GetMapping("/greeting/{name}")
    public test greeting(@PathVariable("name") String name) {
        return new test(counter.incrementAndGet(), String.format(template, name));
    }
}