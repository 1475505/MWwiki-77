package com.liul2566.wiki.service;

import com.liul2566.wiki.domain.Filenames;
import com.liul2566.wiki.mapper.FilenamesMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author ll （ created: 2022-02-15 4:41 )
 */
@Service
public class FilenamesService {
    @Resource
    private FilenamesMapper filenamesMapper;

    public List<Filenames> list() {
        return filenamesMapper.selectByExample(null);
    }
}
