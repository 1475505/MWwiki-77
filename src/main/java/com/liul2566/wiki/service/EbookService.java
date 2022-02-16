package com.liul2566.wiki.service;

import com.liul2566.wiki.domain.Ebook;
import com.liul2566.wiki.domain.EbookExample;
import com.liul2566.wiki.mapper.EbookMapper;
import com.liul2566.wiki.req.EbookReq;
import com.liul2566.wiki.resp.EbookResp;
import com.liul2566.wiki.util.CopyUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author ll （ created: 2022-02-15 4:41 )
 */
@Service
public class EbookService {
    @Resource
    private EbookMapper Ebookmapper;


    public List<EbookResp> list(EbookReq req) {
        EbookExample ebookExample = new EbookExample();
        EbookExample.Criteria criteria = ebookExample.createCriteria();
        criteria.andNameLike("%" + req.getName() + "%");
        List<Ebook> ebookList = Ebookmapper.selectByExample(ebookExample);

        //        List<EbookResp> respList = new ArrayList<>();
//        for (Ebook ebook : ebookList) {
//            EbookResp ebookResp = new EbookResp();//CopyUtil.copy(ebook,EbookResp.class);
//            BeanUtils.copyProperties(ebook, ebookResp);
//            //ebookResp.setId(123L);
//            respList.add(ebookResp);
//        }
        return CopyUtil.copyList(ebookList, EbookResp.class);
    }
}
