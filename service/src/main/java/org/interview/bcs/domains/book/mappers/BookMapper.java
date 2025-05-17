package org.interview.bcs.domains.book.mappers;

import org.apache.ibatis.annotations.Param;
import org.interview.bcs.domains.book.entites.Book;

import java.util.List;

public interface BookMapper {
    int deleteByPrimaryKey( Long bookId );

    int insert( Book record );

    int insertSelective( Book record );

    Book selectByPrimaryKey( Long bookId );

    int updateByPrimaryKeySelective( Book record );

    int updateByPrimaryKeyWithBLOBs( Book record );

    int updateByPrimaryKey( Book record );

    List<Book> select(
            @Param("isbn") String isbn, @Param("title") String title,
            @Param("authorIds") List<Long> authors, @Param("status") Integer status
    );

    Book selectByIsbn( @Param("isbn") String isbn );
}