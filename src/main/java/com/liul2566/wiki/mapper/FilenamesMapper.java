package com.liul2566.wiki.mapper;

import com.liul2566.wiki.domain.Filenames;
import com.liul2566.wiki.domain.FilenamesExample;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface FilenamesMapper {
    long countByExample(FilenamesExample example);

    int deleteByExample(FilenamesExample example);

    int insert(String record);

    int insertSelective(Filenames record);

    List<Filenames> selectByExample(FilenamesExample example);

    int updateByExampleSelective(@Param("record") Filenames record, @Param("example") FilenamesExample example);

    int updateByExample(@Param("record") Filenames record, @Param("example") FilenamesExample example);
}