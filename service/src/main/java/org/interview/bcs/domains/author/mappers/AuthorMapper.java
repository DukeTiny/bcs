package org.interview.bcs.domains.author.mappers;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.interview.bcs.domains.author.entities.Author;

import java.util.List;

@Mapper
public interface AuthorMapper {

    int deleteByPrimaryKey( Long authorId );

    int insert( Author record );

    int insertSelective( Author record );

    Author selectByPrimaryKey( Long authorId );

    int updateByPrimaryKeySelective( Author record );

    int updateByPrimaryKey( Author record );

    List<Author> getAuthorsByName( @Param("name") String name );

}