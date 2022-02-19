package com.liul2566.wiki.controller;

import com.liul2566.wiki.req.DocQueryReq;
import com.liul2566.wiki.req.DocSaveReq;
import com.liul2566.wiki.resp.CommonResp;
import com.liul2566.wiki.resp.DocQueryResp;
import com.liul2566.wiki.resp.PageResp;
import com.liul2566.wiki.service.DocService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

/**
 * @author ll （ created: 2022-02-15 2:10 )
 */
@RestController
@RequestMapping("/Doc")
public class DocController {
    private static final Logger LOG = LoggerFactory.getLogger(DocController.class);


    @Resource
    private DocService Docservice;


    @GetMapping("/list")
    public CommonResp Doc_list(@Valid DocQueryReq req) {
        CommonResp<PageResp<DocQueryResp>> resp = new CommonResp<>();
        PageResp<DocQueryResp> list = Docservice.list(req);
        resp.setContent(list);
        return resp;
    }

    @GetMapping("/find-content/{id}")
    public CommonResp findContent(@PathVariable Long id) {
        CommonResp<String> resp = new CommonResp<>();
        String content = Docservice.findContent(id);
        resp.setContent(content);
        return resp;
    }

    @GetMapping("/all/{ebookId}")
    public CommonResp all(@PathVariable Long ebookId) {
        CommonResp<List<DocQueryResp>> resp = new CommonResp<>();
        List<DocQueryResp> list = Docservice.all(ebookId);
        resp.setContent(list);
        return resp;
    }

    @PostMapping("/save")
    public CommonResp save(@Valid @RequestBody DocSaveReq req) {
        CommonResp resp = new CommonResp<>();
        Docservice.save(req);
        return resp;
    }

    @DeleteMapping("/delete/{idsStr}")//mybatis: ${id}
    public CommonResp delete(@PathVariable String idsStr) {
        CommonResp resp = new CommonResp<>();
        List<String> strings = Arrays.asList(idsStr.split(","));
        Docservice.delete(strings);
        return resp;
    }
}
