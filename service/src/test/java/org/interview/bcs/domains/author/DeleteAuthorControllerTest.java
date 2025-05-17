package org.interview.bcs.domains.author;

import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.interview.bcs.BaseTest;
import org.interview.bcs.api.domains.author.req.DeleteAuthorReq;
import org.interview.bcs.api.domains.author.req.UpsertAuthorReq;
import org.interview.bcs.api.domains.author.resp.UpsertAuthorResp;
import org.interview.bcs.common.BizException;
import org.interview.bcs.domains.author.controller.AuthorController;
import org.interview.bcs.domains.author.entities.Author;
import org.interview.bcs.domains.author.mappers.AuthorMapper;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.handler;

/**
 * @author: wanghu
 * @since: 2025/5/17 10:43
 */
@Slf4j
@WebAppConfiguration
public class DeleteAuthorControllerTest extends BaseTest {

    @Resource
    private AuthorController authorController;

    @Resource
    private AuthorMapper authorMapper;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup( authorController ).build();
    }

    @Test
    void testDeleteAuthor() throws Exception {
        testDelete();
    }

    private void testDelete() throws Exception {
        final String name = "Duke";
        final String nationality = "cn";

        UpsertAuthorReq req = new UpsertAuthorReq();
        req.setAuthorName( name );
        req.setNationality( nationality );

        // 先插入
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post( "/bcs/author/upsert" )
                .accept( MediaType.APPLICATION_JSON_VALUE )
                .contentType( MediaType.APPLICATION_JSON_VALUE )
                .characterEncoding( "UTF-8" )
                .content( JSON.toJSONString( req ) );

        String respStr = mockMvc.perform( request )
                .andExpect( MockMvcResultMatchers.status().isOk() )
                .andExpect( handler().handlerType( AuthorController.class ) )
                .andReturn().getResponse().getContentAsString();
        UpsertAuthorResp resp = JSON.parseObject( respStr, UpsertAuthorResp.class );

        final Long authorId = resp.getAuthorId();
        Author dbAuthor = authorMapper.selectByPrimaryKey( authorId );
        assertThat( dbAuthor ).isNotNull();
        assertThat( dbAuthor.getName() ).isEqualTo( name );
        assertThat( dbAuthor.getNationality() ).isEqualTo( nationality );


        // 删除
        DeleteAuthorReq delReq = new DeleteAuthorReq();
        delReq.setAuthorId( authorId );
        request = MockMvcRequestBuilders
                .delete( "/bcs/author/delete" )
                .accept( MediaType.APPLICATION_JSON_VALUE )
                .contentType( MediaType.APPLICATION_JSON_VALUE )
                .characterEncoding( "UTF-8" )
                .content( JSON.toJSONString( delReq ) );

        mockMvc.perform( request )
                .andExpect( MockMvcResultMatchers.status().isOk() )
                .andExpect( handler().handlerType( AuthorController.class ) )
                .andReturn().getResponse().getContentAsString();

        dbAuthor = authorMapper.selectByPrimaryKey( authorId );
        assertThat( dbAuthor ).isNull();


        // 删除
        delReq.setAuthorId( Long.MAX_VALUE );
        request = MockMvcRequestBuilders
                .delete( "/bcs/author/delete" )
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
    }


}
