package com.liul2566.wiki.controller;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ll （ created: 2022-02-15 2:10 )
 */
@RestController
public class TestController {
    @RequestMapping("/hello")
    public String hello(){
        return "hello";
    }

}
