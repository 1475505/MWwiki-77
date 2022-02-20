package com.liul2566.wiki.mapper;

import org.apache.ibatis.annotations.Param;

/**
 * @author ll （ created: 2022-02-15 4:33 )
 */
public interface DocMapperCust {

   public void increaseViewCount(@Param("id") Long id);

   public void increaseVoteCount(@Param("id") Long id);

}
