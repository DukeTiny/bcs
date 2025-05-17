package org.interview.bcs.domains.book;

import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.interview.bcs.BaseTest;
import org.interview.bcs.api.constant.BookStatusEnum;
import org.interview.bcs.api.domains.book.req.DeleteBookReq;
import org.interview.bcs.api.domains.book.req.UpsertBookReq;
import org.interview.bcs.api.domains.book.resp.UpsertBookResp;
import org.interview.bcs.common.BizException;
import org.interview.bcs.domains.author.controller.AuthorController;
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
import static org.interview.bcs.domains.data.Initializer.AUTHORS;
import static org.interview.bcs.domains.data.Initializer.CATEGORIES;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.handler;

/**
 * @author: wanghu
 * @since: 2025/5/17 10:43
 */
@Slf4j
@WebAppConfiguration
public class DeleteBookControllerTest extends BaseTest {

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
    void testDeleteAuthor() throws Exception {
        testDelete();
    }

    private void testDelete() throws Exception {

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


        // 删除
        DeleteBookReq delReq = new DeleteBookReq();
        delReq.setBookId( bookId );
        request = MockMvcRequestBuilders
                .delete( "/bcs/book/delete" )
                .accept( MediaType.APPLICATION_JSON_VALUE )
                .contentType( MediaType.APPLICATION_JSON_VALUE )
                .characterEncoding( "UTF-8" )
                .content( JSON.toJSONString( delReq ) );

        mockMvc.perform( request )
                .andExpect( MockMvcResultMatchers.status().isOk() )
                .andExpect( handler().handlerType( BookController.class ) )
                .andReturn().getResponse().getContentAsString();

        dbBook = bookMapper.selectByPrimaryKey( bookId );
        assertThat( dbBook ).isNull();


        // 删除
        delReq.setBookId( Long.MAX_VALUE );
        request = MockMvcRequestBuilders
                .delete( "/bcs/book/delete" )
                .accept( MediaType.APPLICATION_JSON_VALUE )
                .contentType( MediaType.APPLICATION_JSON_VALUE )
                .characterEncoding( "UTF-8" )
                .content( JSON.toJSONString( delReq ) );

        try {
            mockMvc.perform( request )
                    .andExpect( handler().handlerType( AuthorController.class ) )
                    .andReturn().getResponse().getContentAsString();
        }
        catch ( Exception e ) {
            assertThat( e.getCause() ).isInstanceOf( BizException.class );
        }

        bookMapper.deleteByPrimaryKey( bookId );
    }


}
