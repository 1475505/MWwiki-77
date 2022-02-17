package com.liul2566.wiki.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.liul2566.wiki.domain.Ebook;
import com.liul2566.wiki.domain.EbookExample;
import com.liul2566.wiki.mapper.EbookMapper;
import com.liul2566.wiki.req.EbookQueryReq;
import com.liul2566.wiki.req.EbookSaveReq;
import com.liul2566.wiki.resp.EbookQueryResp;
import com.liul2566.wiki.resp.PageResp;
import com.liul2566.wiki.util.CopyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ll （ created: 2022-02-15 4:41 )
 */
@Service
public class EbookService {
    @Resource
    private EbookMapper Ebookmapper;

    private static final Logger LOG = LoggerFactory.getLogger(EbookService.class);

    public PageResp<EbookQueryResp> list(EbookQueryReq req) {
        EbookExample ebookExample = new EbookExample();
        EbookExample.Criteria criteria = ebookExample.createCriteria();
        if (!ObjectUtils.isEmpty(req.getName())) {
            criteria.andNameLike("%" + req.getName() + "%");
        }
        List<Ebook> ebookList = Ebookmapper.selectByExample(ebookExample);
        PageHelper.startPage(req.getPage(), req.getSize());
        ebookList = Ebookmapper.selectByExample(ebookExample);

        PageInfo<Ebook> pageInfo = new PageInfo<>(ebookList);
        LOG.info("总行数：{}", pageInfo.getTotal());
        LOG.info("总页数：{}", pageInfo.getPages());

        //        List<EbookResp> respList = new ArrayList<>();
//        for (Ebook ebook : ebookList) {
//            EbookResp ebookResp = new EbookResp();//CopyUtil.copy(ebook,EbookResp.class);
//            BeanUtils.copyProperties(ebook, ebookResp);
//            //ebookResp.setId(123L);
//            respList.add(ebookResp);
//        }
        PageResp<EbookQueryResp> pageResp = new PageResp<>();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(CopyUtil.copyList(ebookList, EbookQueryResp.class));
        return pageResp;
    }

    public List<EbookQueryResp> all(EbookQueryReq req) {
        List<Ebook> ebookList = Ebookmapper.selectByExample(null);
        List<EbookQueryResp> ebookResps = new ArrayList<>();
        ebookResps = CopyUtil.copyList(ebookList, EbookQueryResp.class);
        return ebookResps;
    }

    public void save(EbookSaveReq req) {
        Ebook ebook = CopyUtil.copy(req, Ebook.class);
        if (ObjectUtils.isEmpty(req.getId())) {
            Ebookmapper.insert(ebook);
        } else {
            Ebookmapper.updateByPrimaryKey(ebook);
        }
    }
}
