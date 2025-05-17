package org.interview.bcs.domains.category.mappers;

import org.apache.ibatis.annotations.Param;
import org.interview.bcs.domains.category.entites.Category;

import java.util.List;

public interface CategoryMapper {
    int deleteByPrimaryKey(Long categoryId);

    long insert(Category record);

    long insertSelective(Category record);

    Category selectByPrimaryKey(Long categoryId);

    int updateByPrimaryKeySelective(Category record);

    int updateByPrimaryKey(Category record);

    List<Category> selectAll();

    int loginDelete( @Param ( "categoryId" )Long categoryId );
}