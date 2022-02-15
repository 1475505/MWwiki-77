package com.liul2566.wiki.service;

import com.liul2566.wiki.domain.Demo;
import com.liul2566.wiki.mapper.DemoMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author ll （ created: 2022-02-15 4:41 )
 */
@Service
public class demoService {
    @Resource
    private DemoMapper Demomapper;

    public List<Demo> list(){
        return Demomapper.selectByExample(null);
    }
}
