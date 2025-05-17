package org.interview.bcs.domains.book;

import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.interview.bcs.BaseTest;
import org.interview.bcs.api.constant.BookStatusEnum;
import org.interview.bcs.api.domains.book.resp.GetBookResp;
import org.interview.bcs.api.domains.book.vo.BookVo;
import org.interview.bcs.domains.author.entities.Author;
import org.interview.bcs.domains.book.controller.BookController;
import org.interview.bcs.domains.book.entites.Book;
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
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.interview.bcs.domains.data.Initializer.AUTHORS;
import static org.interview.bcs.domains.data.Initializer.BOOKS;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.handler;

/**
 * @author: wanghu
 * @since: 2025/5/17 10:43
 */
@Slf4j
@WebAppConfiguration
public class GetBookControllerTest extends BaseTest {

    @Resource
    private BookController bookController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup( bookController ).build();
    }

    @Test
    void testGetAuthors() throws Exception {

        testGetWithAllParams();
        testGetWithNonExistsNames();
    }

    private void testGetWithAllParams() throws Exception {

        Map<Long, Author> authorMap = AUTHORS.stream().collect( Collectors.toMap( Author::getAuthorId, author -> author ) );
        Book book = BOOKS.get( random.nextInt( BOOKS.size() ) );
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get( "/bcs/book/get" )
                .accept( MediaType.APPLICATION_JSON_VALUE )
                .characterEncoding( "UTF-8" )
                .param( "isbn", book.getIsbn() )
                .param( "title", book.getTitle() )
                .param( "authorName", authorMap.get( book.getAuthorId() ).getName() )
                .param( "bookStatus", String.valueOf( BookStatusEnum.AVAILABLE.getCode() ) );


        String respStr = mockMvc.perform( request )
                .andExpect( MockMvcResultMatchers.status().isOk() )
                .andExpect( handler().handlerType( BookController.class ) )
                .andReturn().getResponse().getContentAsString();

        GetBookResp resp = JSON.parseObject( respStr, GetBookResp.class );
        List<BookVo> books = resp.getBooks();

        assertThat( books ).hasSize( 1 );
        BookVo bookVo = books.get( 0 );
        assertThat( bookVo.getBookId() ).isEqualTo( book.getBookId() );
        assertThat( bookVo.getIsbn() ).isEqualTo( book.getIsbn() );
        assertThat( bookVo.getAuthorId() ).isEqualTo( book.getAuthorId() );
        assertThat( bookVo.getCategoryId() ).isEqualTo( book.getCategoryId() );
        assertThat( bookVo.getTitle() ).isEqualTo( book.getTitle() );
    }

    private void testGetWithNonExistsNames() throws Exception {

        Map<Long, Author> authorMap = AUTHORS.stream().collect( Collectors.toMap( Author::getAuthorId, author -> author ) );
        Book book = BOOKS.get( random.nextInt( BOOKS.size() ) );
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get( "/bcs/book/get" )
                .accept( MediaType.APPLICATION_JSON_VALUE )
                .characterEncoding( "UTF-8" )
                .param( "isbn", book.getIsbn() )
                .param( "title", book.getTitle() )
                .param( "authorName", "aasdadasdada" )
                .param( "bookStatus", String.valueOf( BookStatusEnum.AVAILABLE.getCode() ) );


        String respStr = mockMvc.perform( request )
                .andExpect( MockMvcResultMatchers.status().isOk() )
                .andExpect( handler().handlerType( BookController.class ) )
                .andReturn().getResponse().getContentAsString();

        GetBookResp resp = JSON.parseObject( respStr, GetBookResp.class );
        List<BookVo> books = resp.getBooks();

        assertThat( books ).hasSize( 0 );
    }

}
