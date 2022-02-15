package com.liul2566.wiki.controller;

import com.liul2566.wiki.domain.Ebook;
import com.liul2566.wiki.resp.CommonResp;
import com.liul2566.wiki.service.EbookService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public CommonResp Ebook_list() {
        CommonResp<List<Ebook>> resp = new CommonResp<>();
        List<Ebook> list = Ebookservice.list();
        resp.setContent(list);
        return resp;
    }
}
