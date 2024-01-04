package ru.aldokimov.spring.spring_rest.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController //@Controller + @ResponseBode над каждым методом
@RequestMapping("/api")
public class FirstRestController {

    @GetMapping("/sayHello")
    public String sayHello() {
        return "Hello world!";
    }
}
