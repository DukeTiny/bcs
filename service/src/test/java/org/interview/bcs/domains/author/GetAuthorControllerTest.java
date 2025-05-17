package org.interview.bcs.domains.author;

import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.interview.bcs.BaseTest;
import org.interview.bcs.api.domains.author.resp.GetAuthorResp;
import org.interview.bcs.api.domains.author.vo.AuthorVo;
import org.interview.bcs.domains.author.controller.AuthorController;
import org.interview.bcs.domains.author.entities.Author;
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
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.interview.bcs.domains.data.Initializer.AUTHORS;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.handler;

/**
 * @author: wanghu
 * @since: 2025/5/17 10:43
 */
@Slf4j
@WebAppConfiguration
public class GetAuthorControllerTest extends BaseTest {

    @Resource
    private AuthorController authorController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup( authorController ).build();
    }

    @Test
    void testGetAuthors() throws Exception {
        testGetWithIdAndName();
        testGetWithName();
        testGetWithId();
    }

    private void testGetWithIdAndName() throws Exception {
        Author author = AUTHORS.get( random.nextInt( AUTHORS.size() ) );
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get( "/bcs/author/get" )
                .accept( MediaType.APPLICATION_JSON_VALUE )
                .characterEncoding( "UTF-8" )
                .param( "authorId", author.getAuthorId().toString() )
                .param( "authorName", author.getName() )
                .param( "pageCurrent", "1" )
                .param( "pageSize", "10" );


        String respStr = mockMvc.perform( request )
                .andExpect( MockMvcResultMatchers.status().isOk() )
                .andExpect( handler().handlerType( AuthorController.class ) )
                .andReturn().getResponse().getContentAsString();

        GetAuthorResp resp = JSON.parseObject( respStr, GetAuthorResp.class );
        List<AuthorVo> authors = resp.getAuthors();

        assertThat( authors ).hasSize( 1 );
        AuthorVo authorVo = authors.get( 0 );
        assertThat( authorVo.getAuthorId() ).isEqualTo( author.getAuthorId() );
        assertThat( authorVo.getAuthorName() ).isEqualTo( author.getName() );
    }

    private void testGetWithName() throws Exception {

        String name = "Julia Child";
        List<Author> multiNames = AUTHORS.stream().filter( a -> a.getName().startsWith( name ) ).collect( Collectors.toList() );
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get( "/bcs/author/get" )
                .accept( MediaType.APPLICATION_JSON_VALUE )
                .characterEncoding( "UTF-8" )
                .param( "authorName", name )
                .param( "pageCurrent", "1" )
                .param( "pageSize", "10" );


        String respStr = mockMvc.perform( request )
                .andExpect( MockMvcResultMatchers.status().isOk() )
                .andExpect( handler().handlerType( AuthorController.class ) )
                .andReturn().getResponse().getContentAsString();

        GetAuthorResp resp = JSON.parseObject( respStr, GetAuthorResp.class );
        List<AuthorVo> authors = resp.getAuthors();

        assertThat( authors ).hasSize( multiNames.size() );
        authors.forEach( author -> {
            assertThat( author.getAuthorName() ).startsWith( name );
        } );
    }

    private void testGetWithId() throws Exception {
        Author author = AUTHORS.get( random.nextInt( AUTHORS.size() ) );
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get( "/bcs/author/get" )
                .accept( MediaType.APPLICATION_JSON_VALUE )
                .characterEncoding( "UTF-8" )
                .param( "authorId", author.getAuthorId().toString() )
                .param( "pageCurrent", "1" )
                .param( "pageSize", "10" );


        String respStr = mockMvc.perform( request )
                .andExpect( MockMvcResultMatchers.status().isOk() )
                .andExpect( handler().handlerType( AuthorController.class ) )
                .andReturn().getResponse().getContentAsString();

        GetAuthorResp resp = JSON.parseObject( respStr, GetAuthorResp.class );
        List<AuthorVo> authors = resp.getAuthors();

        assertThat( authors ).hasSize( 1 );
        AuthorVo authorVo = authors.get( 0 );
        assertThat( authorVo.getAuthorId() ).isEqualTo( author.getAuthorId() );
        assertThat( authorVo.getAuthorName() ).isEqualTo( author.getName() );
    }

}
