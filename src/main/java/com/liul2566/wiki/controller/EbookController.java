package com.liul2566.wiki.controller;

import com.liul2566.wiki.req.EbookQueryReq;
import com.liul2566.wiki.req.EbookSaveReq;
import com.liul2566.wiki.resp.CommonResp;
import com.liul2566.wiki.resp.EbookQueryResp;
import com.liul2566.wiki.resp.PageResp;
import com.liul2566.wiki.service.EbookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author ll （ created: 2022-02-15 2:10 )
 */
@RestController
@RequestMapping("/Ebook")
public class EbookController {
    private static final Logger LOG = LoggerFactory.getLogger(EbookController.class);


    @Resource
    private EbookService Ebookservice;


    @GetMapping("/list")
    public CommonResp Ebook_list(EbookQueryReq req) {
        CommonResp<PageResp<EbookQueryResp>> resp = new CommonResp<>();
        PageResp<EbookQueryResp> list = Ebookservice.list(req);
        resp.setContent(list);
        return resp;
    }

    @GetMapping("/all")
    public CommonResp all(EbookQueryReq req) {
        CommonResp<List<EbookQueryResp>> resp = new CommonResp<>();
        List<EbookQueryResp> list = Ebookservice.all(req);
        resp.setContent(list);
        return resp;
    }

    @PostMapping("/save")
    public CommonResp save(@RequestBody EbookSaveReq req) {
        CommonResp resp = new CommonResp<>();
        Ebookservice.save(req);
        return resp;
    }

    @DeleteMapping("/delete/{id}")//mybatis: ${id}
    public CommonResp delete(@PathVariable Long id) {
        CommonResp resp = new CommonResp<>();
        Ebookservice.delete(id);
        return resp;
    }
}
