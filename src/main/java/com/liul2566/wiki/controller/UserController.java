package com.liul2566.wiki.controller;

import com.liul2566.wiki.req.UserQueryReq;
import com.liul2566.wiki.req.UserResetPasswordReq;
import com.liul2566.wiki.req.UserSaveReq;
import com.liul2566.wiki.resp.CommonResp;
import com.liul2566.wiki.resp.PageResp;
import com.liul2566.wiki.resp.UserQueryResp;
import com.liul2566.wiki.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author ll （ created: 2022-02-15 2:10 )
 */
@RestController
@RequestMapping("/User")
public class UserController {
    private static final Logger LOG = LoggerFactory.getLogger(UserController.class);


    @Resource
    private UserService Userservice;


    @GetMapping("/list")
    public CommonResp User_list(@Valid UserQueryReq req) {
        CommonResp<PageResp<UserQueryResp>> resp = new CommonResp<>();
        PageResp<UserQueryResp> list = Userservice.list(req);
        resp.setContent(list);
        return resp;
    }

    @GetMapping("/all")
    public CommonResp all(UserQueryReq req) {
        CommonResp<List<UserQueryResp>> resp = new CommonResp<>();
        List<UserQueryResp> list = Userservice.all(req);
        resp.setContent(list);
        return resp;
    }

    @PostMapping("/save")
    public CommonResp save(@Valid @RequestBody UserSaveReq req) {
        req.setPassword(DigestUtils.md5DigestAsHex(req.getPassword().getBytes(StandardCharsets.UTF_8)));
        CommonResp resp = new CommonResp<>();
        Userservice.save(req);
        return resp;
    }

    @PostMapping("/reset-password")
    public CommonResp resetPassword(@Valid @RequestBody UserResetPasswordReq req) {
        req.setPassword(DigestUtils.md5DigestAsHex(req.getPassword().getBytes(StandardCharsets.UTF_8)));
        CommonResp resp = new CommonResp<>();
        Userservice.resetPassword(req);
        return resp;
    }

    @DeleteMapping("/delete/{id}")//mybatis: ${id}
    public CommonResp delete(@PathVariable Long id) {
        CommonResp resp = new CommonResp<>();
        Userservice.delete(id);
        return resp;
    }
}
