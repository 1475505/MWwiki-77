package com.liul2566.wiki.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.liul2566.wiki.domain.User;
import com.liul2566.wiki.domain.UserExample;
import com.liul2566.wiki.exception.BusinessException;
import com.liul2566.wiki.exception.BusinessExceptionCode;
import com.liul2566.wiki.mapper.UserMapper;
import com.liul2566.wiki.req.UserQueryReq;
import com.liul2566.wiki.req.UserSaveReq;
import com.liul2566.wiki.resp.PageResp;
import com.liul2566.wiki.resp.UserQueryResp;
import com.liul2566.wiki.util.CopyUtil;
import com.liul2566.wiki.util.SnowFlake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ll （ created: 2022-02-15 4:41 )
 */
@Service
public class UserService {
    @Resource
    private UserMapper Usermapper;

    @Autowired// same as @resource
    private SnowFlake snowFlake;

    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    public PageResp<UserQueryResp> list(UserQueryReq req) {
        UserExample userExample = new UserExample();
        UserExample.Criteria criteria = userExample.createCriteria();
        if (!ObjectUtils.isEmpty(req.getLoginName())) {
            criteria.andNameLike("%" + req.getLoginName() + "%");
        }
        List<User> userList = Usermapper.selectByExample(userExample);

        PageHelper.startPage(req.getPage(), req.getSize());
        userList = Usermapper.selectByExample(userExample);

        PageInfo<User> pageInfo = new PageInfo<>(userList);
        LOG.info("总行数：{}", pageInfo.getTotal());
        LOG.info("总页数：{}", pageInfo.getPages());

        //        List<UserResp> respList = new ArrayList<>();
//        for (User user : userList) {
//            UserResp userResp = new UserResp();//CopyUtil.copy(user,UserResp.class);
//            BeanUtils.copyProperties(user, userResp);
//            //userResp.setId(123L);
//            respList.add(userResp);
//        }
        PageResp<UserQueryResp> pageResp = new PageResp<>();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(CopyUtil.copyList(userList, UserQueryResp.class));
        return pageResp;
    }

    public List<UserQueryResp> all(UserQueryReq req) {
        List<User> userList = Usermapper.selectByExample(null);
        List<UserQueryResp> userResps = new ArrayList<>();
        userResps = CopyUtil.copyList(userList, UserQueryResp.class);
        return userResps;
    }

    public void save(UserSaveReq req) {
        User user = CopyUtil.copy(req, User.class);
        if (ObjectUtils.isEmpty(req.getId())) {
            if (ObjectUtils.isEmpty(selectByLoginName(req.getLoginName()))) {
                user.setId(snowFlake.nextId());
                Usermapper.insert(user);
            } else {
                throw new BusinessException(BusinessExceptionCode.USER_LOGIN_NAME_EXIST);
            }
        } else {
            Usermapper.updateByPrimaryKey(user);
        }
    }

    public User selectByLoginName(String LoginName) {
        UserExample userExample = new UserExample();
        UserExample.Criteria criteria = userExample.createCriteria();
        criteria.andLoginNameEqualTo(LoginName);
        List<User> userList = Usermapper.selectByExample(userExample);
        if (CollectionUtils.isEmpty(userList)) {
            return null;
        } else {
            return userList.get(0);
        }
    }

    public void delete(Long id) {
        Usermapper.deleteByPrimaryKey(id);
    }
}
