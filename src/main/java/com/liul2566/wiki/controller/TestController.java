package com.liul2566.wiki.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ll （ created: 2022-02-15 2:10 )
 */
@RestController
public class TestController {
    private static final Logger LOG = LoggerFactory.getLogger(TestController.class);

    @Value("${test.hello:TEST}")
    private String testHello;

    @RequestMapping("/hello")
    public String hello(){
        return "hello";
    }

    @PostMapping("/hello/post")
    public String hello(String name){
        return "Hello-a " + name;
    }
}
