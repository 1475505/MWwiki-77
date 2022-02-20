package com.liul2566.wiki.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.liul2566.wiki.domain.Content;
import com.liul2566.wiki.domain.Doc;
import com.liul2566.wiki.domain.DocExample;
import com.liul2566.wiki.exception.BusinessException;
import com.liul2566.wiki.exception.BusinessExceptionCode;
import com.liul2566.wiki.mapper.ContentMapper;
import com.liul2566.wiki.mapper.DocMapper;
import com.liul2566.wiki.mapper.DocMapperCust;
import com.liul2566.wiki.req.DocQueryReq;
import com.liul2566.wiki.req.DocSaveReq;
import com.liul2566.wiki.resp.DocQueryResp;
import com.liul2566.wiki.resp.PageResp;
import com.liul2566.wiki.util.CopyUtil;
import com.liul2566.wiki.util.RedisUtil;
import com.liul2566.wiki.util.RequestContext;
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
public class DocService {
    @Resource
    private DocMapper Docmapper;
    @Resource
    private ContentMapper contentMapper;
    @Resource
    private DocMapperCust docMapperCust;
    @Autowired// same as @resource
    private SnowFlake snowFlake;

    @Autowired
    private RedisUtil redisUtil;

    private static final Logger LOG = LoggerFactory.getLogger(DocService.class);

    public PageResp<DocQueryResp> list(DocQueryReq req) {
        DocExample docExample = new DocExample();
        docExample.setOrderByClause("sort asc");
        DocExample.Criteria criteria = docExample.createCriteria();
        if (!ObjectUtils.isEmpty(req.getName())) {
            criteria.andNameLike("%" + req.getName() + "%");
        }
        List<Doc> docList = Docmapper.selectByExample(docExample);
        PageHelper.startPage(req.getPage(), req.getSize());
        docList = Docmapper.selectByExample(docExample);

        PageInfo<Doc> pageInfo = new PageInfo<>(docList);
        LOG.info("总行数：{}", pageInfo.getTotal());
        LOG.info("总页数：{}", pageInfo.getPages());

        //        List<DocResp> respList = new ArrayList<>();
//        for (Doc doc : docList) {
//            DocResp docResp = new DocResp();//CopyUtil.copy(doc,DocResp.class);
//            BeanUtils.copyProperties(doc, docResp);
//            //docResp.setId(123L);
//            respList.add(docResp);
//        }
        PageResp<DocQueryResp> pageResp = new PageResp<>();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(CopyUtil.copyList(docList, DocQueryResp.class));
        return pageResp;
    }

    public List<DocQueryResp> all(Long ebookId) {
        DocExample docExample = new DocExample();
        DocExample.createCriteria().andEbookIdEqualTo(ebookId);
        docExample.setOrderByClause("sort asc");
        List<Doc> docList = Docmapper.selectByExample(docExample);

        List<DocQueryResp> list = CopyUtil.copyList(docList, DocQueryResp.class);
        return list;
    }

    public void save(DocSaveReq req) {
        Doc doc = CopyUtil.copy(req, Doc.class);
        Content content = CopyUtil.copy(req, Content.class);
        if (ObjectUtils.isEmpty(req.getId())) {
            doc.setId(snowFlake.nextId());
            Docmapper.insert(doc);
            doc.setViewCount(0);
            doc.setVoteCount(0);
            content.setId(doc.getId());//!!ID需要为同一个值，不要雪花生成
            contentMapper.insert(content);
        } else { //update
            Docmapper.updateByPrimaryKey(doc);
            int count = contentMapper.updateByPrimaryKeyWithBLOBs(content);//BLOB - 大字段（富文本）
            if (count == 0) {
                contentMapper.insert(content);
            }
        }
    }

    public void delete(Long id) {
        Docmapper.deleteByPrimaryKey(id);
    }

    public void delete(List<String> ids) {
        DocExample docExample = new DocExample();
        DocExample.createCriteria().andIdIn(ids);
        Docmapper.deleteByExample(docExample);
    }

    public String findContent(Long id) {
        Content content = contentMapper.selectByPrimaryKey(id);
        docMapperCust.increaseViewCount(id);
        if (ObjectUtils.isEmpty(content)) {
            return "";
        } else {
            return content.getContent();
        }
    }

    public void vote(Long id) {
        String ip = RequestContext.getRemoteAddr();
        if (redisUtil.validateRepeat("DOC_VOTE_" + id + "_" + ip, 3600 * 8)) {
            docMapperCust.increaseVoteCount(id);
        } else {
            throw new BusinessException(BusinessExceptionCode.VOTE_REPEAT);
        }
        docMapperCust.increaseViewCount(id);
    }
}
