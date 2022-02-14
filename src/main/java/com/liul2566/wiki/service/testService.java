package com.liul2566.wiki.service;

import com.liul2566.wiki.domain.test;
import com.liul2566.wiki.mapper.testMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author ll （ created: 2022-02-15 4:41 )
 */
@Service
public class testService {
    @Resource
    private testMapper testmapper;

    public List<test> list(){
        return testmapper.list();
    }
}
