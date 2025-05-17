package org.interview.bcs.domains.book;

import lombok.extern.slf4j.Slf4j;
import org.assertj.core.util.Lists;
import org.interview.bcs.BaseTest;
import org.interview.bcs.api.constant.BookStatusEnum;
import org.interview.bcs.domains.author.entities.Author;
import org.interview.bcs.domains.book.entites.Book;
import org.interview.bcs.domains.book.mappers.BookMapper;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.interview.bcs.domains.data.Initializer.*;

/**
 * @author: wanghu
 * @since: 2025/5/17 10:13
 */
@Slf4j
public class BookMapperTest extends BaseTest {

    @Resource
    private BookMapper bookMapper;

    @Test
    void testSelect() {
        BOOKS.forEach( book -> {
            List<Book> dbBooks = bookMapper.select( book.getIsbn(), book.getTitle(), Lists.newArrayList( book.getAuthorId() ), book.getStatus() );
            assertThat( dbBooks ).hasSize( 1 );
            Book firstB = dbBooks.get( 0 );
            assertThat( firstB.getBookId() ).isEqualTo( book.getBookId() );
            assertThat( firstB.getIsbn() ).isEqualTo( book.getIsbn() );
            assertThat( firstB.getTitle() ).isEqualTo( book.getTitle() );
            assertThat( firstB.getAuthorId() ).isEqualTo( book.getAuthorId() );
            assertThat( firstB.getCategoryId() ).isEqualTo( book.getCategoryId() );
        } );
    }

    @Test
    void testSelectByIsbn() {
        final List<Book> books = BOOKS;
        books.forEach( book -> {
            Book dbBook = bookMapper.selectByIsbn( book.getIsbn() );
            assertThat( dbBook ).isNotNull();
        } );


        Book book = getBook();
        bookMapper.insertSelective( book );

        Book dbBook = bookMapper.selectByIsbn( book.getIsbn() );
        assertThat( dbBook ).isNotNull();
        Book logicDelete = new Book();
        logicDelete.setBookId( book.getBookId() );
        logicDelete.setDisable( true );
        bookMapper.updateByPrimaryKeySelective( logicDelete );

        dbBook = bookMapper.selectByIsbn( book.getIsbn() );
        assertThat( dbBook ).isNull();
    }

    private Book getBook() {
        Author author = AUTHORS.get( random.nextInt( AUTHORS.size() ) );
        Book book = new Book();
        book.setIsbn( "ISBN-123" );
        book.setTitle( "My Father" );
        book.setAuthorId( author.getAuthorId() );
        book.setPublisherId( 1L );
        book.setPublicationYear( 2025 );
        book.setCategoryId( CATEGORIES.get( random.nextInt( CATEGORIES.size() ) ).getCategoryId() );
        book.setLanguage( author.getNationality() );
        book.setPageCount( 100 );
        book.setCoverImage( "" );
        book.setStatus( BookStatusEnum.AVAILABLE.getCode() );
        book.setDescription( "My description" );
        return book;
    }

}
