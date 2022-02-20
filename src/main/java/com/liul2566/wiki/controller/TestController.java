package com.liul2566.wiki.controller;

import com.liul2566.wiki.domain.test;
import com.liul2566.wiki.service.testService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author ll （ created: 2022-02-15 2:10 )
 */
@RestController
public class TestController {
    private static final Logger LOG = LoggerFactory.getLogger(TestController.class);

    @Value("${test.hello:TEST}")
    private String testHello;
    @Resource
    private RedisTemplate redisTemplate;

    @Resource
    private testService testservice;

    @RequestMapping("/hello")
    public String hello() {
        return "hello";
    }

    @PostMapping("/hello/post")
    public String hello(String name) {
        return "Hello-a " + name;
    }

    @GetMapping("/test/list")
    public List<test> test_list() {
        return testservice.list();
    }

    @RequestMapping("/redis/set/{key}/{value}")
    public String set(@PathVariable Long key, @PathVariable String value) {
        redisTemplate.opsForValue().set(key, value, 3600, TimeUnit.SECONDS);
        LOG.info("key: {}, value: {}", key, value);
        return "success";
    }

    @RequestMapping("/redis/get/{key}")
    public Object get(@PathVariable Long key) {
        Object object = redisTemplate.opsForValue().get(key);
        LOG.info("key: {}, value: {}", key, object);
        return object;
    }
}
