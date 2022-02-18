package com.liul2566.wiki.controller;

import com.liul2566.wiki.req.CategoryQueryReq;
import com.liul2566.wiki.req.CategorySaveReq;
import com.liul2566.wiki.resp.CategoryQueryResp;
import com.liul2566.wiki.resp.CommonResp;
import com.liul2566.wiki.resp.PageResp;
import com.liul2566.wiki.service.CategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * @author ll （ created: 2022-02-15 2:10 )
 */
@RestController
@RequestMapping("/Category")
public class CategoryController {
    private static final Logger LOG = LoggerFactory.getLogger(CategoryController.class);


    @Resource
    private CategoryService Categoryservice;


    @GetMapping("/list")
    public CommonResp Category_list(@Valid CategoryQueryReq req) {
        CommonResp<PageResp<CategoryQueryResp>> resp = new CommonResp<>();
        PageResp<CategoryQueryResp> list = Categoryservice.list(req);
        resp.setContent(list);
        return resp;
    }

    @GetMapping("/all")
    public CommonResp all(CategoryQueryReq req) {
        CommonResp<List<CategoryQueryResp>> resp = new CommonResp<>();
        List<CategoryQueryResp> list = Categoryservice.all(req);
        resp.setContent(list);
        return resp;
    }

    @PostMapping("/save")
    public CommonResp save(@Valid @RequestBody CategorySaveReq req) {
        CommonResp resp = new CommonResp<>();
        Categoryservice.save(req);
        return resp;
    }

    @DeleteMapping("/delete/{id}")//mybatis: ${id}
    public CommonResp delete(@PathVariable Long id) {
        CommonResp resp = new CommonResp<>();
        Categoryservice.delete(id);
        return resp;
    }
}
