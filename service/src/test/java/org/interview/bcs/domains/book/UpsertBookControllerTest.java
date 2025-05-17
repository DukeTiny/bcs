package org.interview.bcs.domains.book;

import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.interview.bcs.BaseTest;
import org.interview.bcs.api.constant.BookStatusEnum;
import org.interview.bcs.api.domains.book.req.UpsertBookReq;
import org.interview.bcs.api.domains.book.resp.UpsertBookResp;
import org.interview.bcs.common.BizException;
import org.interview.bcs.domains.author.entities.Author;
import org.interview.bcs.domains.book.controller.BookController;
import org.interview.bcs.domains.book.entites.Book;
import org.interview.bcs.domains.book.mappers.BookMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.annotation.Resource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.interview.bcs.domains.data.Initializer.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.handler;

/**
 * @author: wanghu
 * @since: 2025/5/17 10:43
 */
@Slf4j
@WebAppConfiguration
public class UpsertBookControllerTest extends BaseTest {

    @Resource
    private BookController bookController;

    @Resource
    private BookMapper bookMapper;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup( bookController ).build();
    }

    @Test
    void testUpsertBook() throws Exception {
        testUpsert();
        testNoExists();
        testIsbnExists();
        testAuthorNotExists();
        testCategoryNotExists();
    }

    private void testUpsert() throws Exception {

        Author author = AUTHORS.get( random.nextInt( AUTHORS.size() ) );

        UpsertBookReq req = new UpsertBookReq();
        req.setIsbn("ISBN-AAABBBCCC");
        req.setTitle("Test Title");
        req.setAuthorId( author.getAuthorId() );
        req.setPublisherId(11L);
        req.setPublicationYear(2025);
        req.setCategoryId( CATEGORIES.get( random.nextInt(CATEGORIES.size()) ).getCategoryId() );
        req.setLanguage( author.getNationality() );
        req.setPageCount(100);
        req.setCoverImage("");
        req.setBookStatus( BookStatusEnum.AVAILABLE );
        req.setDescription( "Test Description" );


        // 先插入
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post( "/bcs/book/upsert" )
                .accept( MediaType.APPLICATION_JSON_VALUE )
                .contentType( MediaType.APPLICATION_JSON_VALUE )
                .characterEncoding( "UTF-8" )
                .content( JSON.toJSONString( req ) );

        String respStr = mockMvc.perform( request )
                .andExpect( MockMvcResultMatchers.status().isOk() )
                .andExpect( handler().handlerType( BookController.class ) )
                .andReturn().getResponse().getContentAsString();
        UpsertBookResp resp = JSON.parseObject( respStr, UpsertBookResp.class );

        final Long bookId = resp.getBookId();
        Book dbBook = bookMapper.selectByPrimaryKey( bookId );
        assertThat( dbBook ).isNotNull();
        assertThat( dbBook.getTitle() ).isEqualTo( req.getTitle() );
        assertThat( dbBook.getIsbn() ).isEqualTo( req.getIsbn() );
        assertThat( dbBook.getCategoryId() ).isEqualTo( req.getCategoryId() );
        assertThat( dbBook.getAuthorId() ).isEqualTo( req.getAuthorId() );
        assertThat( dbBook.getLanguage() ).isEqualTo( req.getLanguage() );
        assertThat( dbBook.getPublisherId() ).isEqualTo( req.getPublisherId() );
        assertThat( dbBook.getPublicationYear() ).isEqualTo( req.getPublicationYear() );
        assertThat( dbBook.getPageCount() ).isEqualTo( req.getPageCount() );
        assertThat( dbBook.getCoverImage() ).isEqualTo( req.getCoverImage() );


        author = AUTHORS.get( random.nextInt( AUTHORS.size() ) );
        // 后更新
        req.setBookId( bookId );
        req.setIsbn("ISBN-UPDATE");
        req.setTitle("Test Title_UPDATE");
        req.setAuthorId( author.getAuthorId() );
        req.setPublisherId(12L);
        req.setPublicationYear(2024);
        req.setCategoryId( CATEGORIES.get( random.nextInt(CATEGORIES.size()) ).getCategoryId() );
        req.setLanguage( author.getNationality() );
        req.setPageCount(101);
        req.setCoverImage("_UPDATE");
        req.setBookStatus( BookStatusEnum.LOST );
        req.setDescription( "Test Description_UPDATE" );



        request = MockMvcRequestBuilders
                .post( "/bcs/book/upsert" )
                .accept( MediaType.APPLICATION_JSON_VALUE )
                .contentType( MediaType.APPLICATION_JSON_VALUE )
                .characterEncoding( "UTF-8" )
                .content( JSON.toJSONString( req ) );

        respStr = mockMvc.perform( request )
                .andExpect( MockMvcResultMatchers.status().isOk() )
                .andExpect( handler().handlerType( BookController.class ) )
                .andReturn().getResponse().getContentAsString();
        resp = JSON.parseObject( respStr, UpsertBookResp.class );

        dbBook = bookMapper.selectByPrimaryKey( bookId );
        assertThat( dbBook ).isNotNull();
        assertThat( dbBook.getTitle() ).isEqualTo( req.getTitle() );
        assertThat( dbBook.getIsbn() ).isEqualTo( req.getIsbn() );
        assertThat( dbBook.getCategoryId() ).isEqualTo( req.getCategoryId() );
        assertThat( dbBook.getAuthorId() ).isEqualTo( req.getAuthorId() );
        assertThat( dbBook.getLanguage() ).isEqualTo( req.getLanguage() );
        assertThat( dbBook.getPublisherId() ).isEqualTo( req.getPublisherId() );
        assertThat( dbBook.getPublicationYear() ).isEqualTo( req.getPublicationYear() );
        assertThat( dbBook.getPageCount() ).isEqualTo( req.getPageCount() );
        assertThat( dbBook.getCoverImage() ).isEqualTo( req.getCoverImage() );

        bookMapper.deleteByPrimaryKey( bookId );
    }

    private void testNoExists() throws Exception {

        Author author = AUTHORS.get( random.nextInt( AUTHORS.size() ) );

        UpsertBookReq req = new UpsertBookReq();
        req.setBookId( Long.MAX_VALUE );
        req.setIsbn("ISBN-AAABBBCCC");
        req.setTitle("Test Title");
        req.setAuthorId( author.getAuthorId() );
        req.setPublisherId(11L);
        req.setPublicationYear(2025);
        req.setCategoryId( CATEGORIES.get( random.nextInt(CATEGORIES.size()) ).getCategoryId() );
        req.setLanguage( author.getNationality() );
        req.setPageCount(100);
        req.setCoverImage("");
        req.setBookStatus( BookStatusEnum.AVAILABLE );
        req.setDescription( "Test Description" );


        // 先插入
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post( "/bcs/book/upsert" )
                .accept( MediaType.APPLICATION_JSON_VALUE )
                .contentType( MediaType.APPLICATION_JSON_VALUE )
                .characterEncoding( "UTF-8" )
                .content( JSON.toJSONString( req ) );


        try {
            mockMvc.perform( request )
                    .andExpect( handler().handlerType( BookController.class ) )
                    .andReturn().getResponse().getContentAsString();
        }
        catch ( Exception e ) {
            assertThat( e.getCause() ).isInstanceOf( BizException.class );
        }

    }

    private void testIsbnExists() throws Exception {

        Author author = AUTHORS.get( random.nextInt( AUTHORS.size() ) );

        UpsertBookReq req = new UpsertBookReq();
        req.setBookId( 1L );
        req.setIsbn( BOOKS.get( random.nextInt(BOOKS.size()) ).getIsbn() );
        req.setTitle("Test Title");
        req.setAuthorId( author.getAuthorId() );
        req.setPublisherId(11L);
        req.setPublicationYear(2025);
        req.setCategoryId( CATEGORIES.get( random.nextInt(CATEGORIES.size()) ).getCategoryId() );
        req.setLanguage( author.getNationality() );
        req.setPageCount(100);
        req.setCoverImage("");
        req.setBookStatus( BookStatusEnum.AVAILABLE );
        req.setDescription( "Test Description" );


        // 先插入
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post( "/bcs/book/upsert" )
                .accept( MediaType.APPLICATION_JSON_VALUE )
                .contentType( MediaType.APPLICATION_JSON_VALUE )
                .characterEncoding( "UTF-8" )
                .content( JSON.toJSONString( req ) );

        try {
            mockMvc.perform( request )
                    .andExpect( MockMvcResultMatchers.status().isOk() )
                    .andExpect( handler().handlerType( BookController.class ) )
                    .andReturn().getResponse().getContentAsString();
        }
        catch ( Exception e ) {
            assertThat( e.getCause() ).isInstanceOf( BizException.class );
        }

    }

    private void testAuthorNotExists() throws Exception {

        Author author = AUTHORS.get( random.nextInt( AUTHORS.size() ) );
        Book book = BOOKS.get( random.nextInt( BOOKS.size() ) );
        UpsertBookReq req = new UpsertBookReq();
        req.setBookId( book.getBookId() );
        req.setIsbn( book.getIsbn() );
        req.setTitle("Test Title");
        req.setAuthorId( Long.MAX_VALUE );
        req.setPublisherId(11L);
        req.setPublicationYear(2025);
        req.setCategoryId( CATEGORIES.get( random.nextInt(CATEGORIES.size()) ).getCategoryId() );
        req.setLanguage( author.getNationality() );
        req.setPageCount(100);
        req.setCoverImage("");
        req.setBookStatus( BookStatusEnum.AVAILABLE );
        req.setDescription( "Test Description" );


        // 先插入
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post( "/bcs/book/upsert" )
                .accept( MediaType.APPLICATION_JSON_VALUE )
                .contentType( MediaType.APPLICATION_JSON_VALUE )
                .characterEncoding( "UTF-8" )
                .content( JSON.toJSONString( req ) );


        try {
            mockMvc.perform( request )
                    .andExpect( MockMvcResultMatchers.status().isOk() )
                    .andExpect( handler().handlerType( BookController.class ) )
                    .andReturn().getResponse().getContentAsString();
        }
        catch ( Exception e ) {
            assertThat( e.getCause() ).isInstanceOf( BizException.class );
        }

    }

    private void testCategoryNotExists() throws Exception {

        Author author = AUTHORS.get( random.nextInt( AUTHORS.size() ) );
        Book book = BOOKS.get( random.nextInt( BOOKS.size() ) );
        UpsertBookReq req = new UpsertBookReq();
        req.setBookId( book.getBookId() );
        req.setIsbn( book.getIsbn() );
        req.setAuthorId( author.getAuthorId() );
        req.setTitle("Test Title");
        req.setPublisherId(11L);
        req.setPublicationYear(2025);
        req.setCategoryId( Long.MAX_VALUE );
        req.setLanguage( author.getNationality() );
        req.setPageCount(100);
        req.setCoverImage("");
        req.setBookStatus( BookStatusEnum.AVAILABLE );
        req.setDescription( "Test Description" );


        // 先插入
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post( "/bcs/book/upsert" )
                .accept( MediaType.APPLICATION_JSON_VALUE )
                .contentType( MediaType.APPLICATION_JSON_VALUE )
                .characterEncoding( "UTF-8" )
                .content( JSON.toJSONString( req ) );


        try {
            mockMvc.perform( request )
                    .andExpect( MockMvcResultMatchers.status().isOk() )
                    .andExpect( handler().handlerType( BookController.class ) )
                    .andReturn().getResponse().getContentAsString();
        }
        catch ( Exception e ) {
            assertThat( e.getCause() ).isInstanceOf( BizException.class );
        }

    }


}
