package com.liul2566.wiki.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.liul2566.wiki.domain.Category;
import com.liul2566.wiki.domain.CategoryExample;
import com.liul2566.wiki.mapper.CategoryMapper;
import com.liul2566.wiki.req.CategoryQueryReq;
import com.liul2566.wiki.req.CategorySaveReq;
import com.liul2566.wiki.resp.CategoryQueryResp;
import com.liul2566.wiki.resp.PageResp;
import com.liul2566.wiki.util.CopyUtil;
import com.liul2566.wiki.util.SnowFlake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author ll （ created: 2022-02-15 4:41 )
 */
@Service
public class CategoryService {
    @Resource
    private CategoryMapper Categorymapper;

    @Autowired// same as @resource
    private SnowFlake snowFlake;

    private static final Logger LOG = LoggerFactory.getLogger(CategoryService.class);

    public PageResp<CategoryQueryResp> list(CategoryQueryReq req) {
        CategoryExample categoryExample = new CategoryExample();
        CategoryExample.Criteria criteria = categoryExample.createCriteria();
        if (!ObjectUtils.isEmpty(req.getName())) {
            criteria.andNameLike("%" + req.getName() + "%");
        }
        List<Category> categoryList = Categorymapper.selectByExample(categoryExample);
        PageHelper.startPage(req.getPage(), req.getSize());
        categoryList = Categorymapper.selectByExample(categoryExample);

        PageInfo<Category> pageInfo = new PageInfo<>(categoryList);
        LOG.info("总行数：{}", pageInfo.getTotal());
        LOG.info("总页数：{}", pageInfo.getPages());

        //        List<CategoryResp> respList = new ArrayList<>();
//        for (Category category : categoryList) {
//            CategoryResp categoryResp = new CategoryResp();//CopyUtil.copy(category,CategoryResp.class);
//            BeanUtils.copyProperties(category, categoryResp);
//            //categoryResp.setId(123L);
//            respList.add(categoryResp);
//        }
        PageResp<CategoryQueryResp> pageResp = new PageResp<>();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(CopyUtil.copyList(categoryList, CategoryQueryResp.class));
        return pageResp;
    }

    public List<CategoryQueryResp> all(CategoryQueryReq req) {
        CategoryExample categoryExample = new CategoryExample();
        CategoryExample.Criteria criteria = categoryExample.createCriteria();
        List<Category> categoryList = Categorymapper.selectByExample(categoryExample);

        List<CategoryQueryResp> list = CopyUtil.copyList(categoryList, CategoryQueryResp.class);
        return list;
    }

    public void save(CategorySaveReq req) {
        Category category = CopyUtil.copy(req, Category.class);
        if (ObjectUtils.isEmpty(req.getId())) {
            category.setId(snowFlake.nextId());
            Categorymapper.insert(category);
        } else {
            Categorymapper.updateByPrimaryKey(category);
        }
    }

    public void delete(Long id) {
        Categorymapper.deleteByPrimaryKey(id);
    }
}
