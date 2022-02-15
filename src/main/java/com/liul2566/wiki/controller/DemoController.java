package com.liul2566.wiki.controller;

import com.liul2566.wiki.domain.Demo;
import com.liul2566.wiki.service.demoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.nio.file.DirectoryNotEmptyException;
import java.util.List;

/**
 * @author ll （ created: 2022-02-15 2:10 )
 */
@RestController
@RequestMapping("/demo")
public class DemoController {
    private static final Logger LOG = LoggerFactory.getLogger(DemoController.class);


    @Resource
    private demoService Demoservice;


    @GetMapping("/list")
    public List<Demo> Demo_list() {
        return Demoservice.list();
    }
}
