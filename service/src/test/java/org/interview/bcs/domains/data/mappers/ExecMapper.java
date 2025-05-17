package org.interview.bcs.domains.data.mappers;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @author: wanghu
 * @since: 2025/5/16 23:08
 */
@Mapper
public interface ExecMapper {

    @Select({"${sql}"})
    void execute( @Param("sql") String sql );

}
