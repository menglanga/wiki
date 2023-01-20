package com.leihao.wiki.mapper;

import org.apache.ibatis.annotations.Param;

public interface DocMapperCustom {
    void increaseViewCount(@Param("id") Long id);

    void increaseVoteCount(@Param("id") Long id);
}
