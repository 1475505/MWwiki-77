package com.liul2566.wiki.service;

import com.liul2566.wiki.domain.Ebook;
import com.liul2566.wiki.mapper.EbookMapper;
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

    public List<Ebook> list() {
        return Ebookmapper.selectByExample(null);
    }
}
