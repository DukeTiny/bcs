package org.interview.bcs.domains.category;

import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.interview.bcs.BaseTest;
import org.interview.bcs.api.base.BaseReq;
import org.interview.bcs.api.constant.Constants;
import org.interview.bcs.api.domains.category.req.DeleteCategoryReq;
import org.interview.bcs.api.domains.category.req.UpsertCategoryReq;
import org.interview.bcs.api.domains.category.resp.UpsertCategoryResp;
import org.interview.bcs.common.BizException;
import org.interview.bcs.domains.category.controller.CategoryController;
import org.interview.bcs.domains.category.entites.Category;
import org.interview.bcs.domains.category.mappers.CategoryMapper;
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
public class DeleteCategoryControllerTest extends BaseTest {

    @Resource
    private CategoryController categoryController;

    @Resource
    private CategoryMapper categoryMapper;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup( categoryController ).build();
    }

    @Test
    void testDeleteAuthor() throws Exception {
        testDelete();
    }

    private void testDelete() throws Exception {

        UpsertCategoryReq req = new UpsertCategoryReq();
        req.setCategoryName("Category:Test1");
        req.setParentId( Constants.ROOT_CATEGORY_ID );
        req.setDescription("Category:Description1");


        final Long categoryId = insert( req );
        Category dbCategory = categoryMapper.selectByPrimaryKey( categoryId );
        assertThat( dbCategory ).isNotNull();
        assertThat( dbCategory.getName() ).isEqualTo( req.getCategoryName() );
        assertThat( dbCategory.getDescription() ).isEqualTo( req.getDescription() );
        assertThat( dbCategory.getParentId() ).isEqualTo( Constants.ROOT_CATEGORY_ID );


        req.setCategoryName("Category:Test:Child1");
        req.setParentId( categoryId );
        req.setDescription("Category:Description:Child1");
        final Long firstChildCategoryId = insert( req );
        Category firstDbCategoryChild = categoryMapper.selectByPrimaryKey( firstChildCategoryId );
        assertThat( firstDbCategoryChild ).isNotNull();

        req.setCategoryName("Category:Test:Child2");
        req.setParentId( categoryId );
        req.setDescription("Category:Description:Child2");
        final Long secondChildCategoryId = insert( req );
        Category secondDbCategoryChild = categoryMapper.selectByPrimaryKey( secondChildCategoryId );
        assertThat( secondDbCategoryChild ).isNotNull();



        // 删除
        DeleteCategoryReq delReq = new DeleteCategoryReq();
        delReq.setCategoryId( categoryId );
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .delete( "/bcs/category/delete" )
                .accept( MediaType.APPLICATION_JSON_VALUE )
                .contentType( MediaType.APPLICATION_JSON_VALUE )
                .characterEncoding( "UTF-8" )
                .content( JSON.toJSONString( delReq ) );

        mockMvc.perform( request )
                .andExpect( MockMvcResultMatchers.status().isOk() )
                .andExpect( handler().handlerType( CategoryController.class ) )
                .andReturn().getResponse().getContentAsString();


        dbCategory = categoryMapper.selectByPrimaryKey( categoryId );
        firstDbCategoryChild = categoryMapper.selectByPrimaryKey( firstChildCategoryId );
        secondDbCategoryChild = categoryMapper.selectByPrimaryKey( secondChildCategoryId );
        assertThat( dbCategory ).isNull();
        assertThat( firstDbCategoryChild ).isNull();
        assertThat( secondDbCategoryChild ).isNull();

        // 不存在删除
        delReq.setCategoryId( Long.MAX_VALUE );
        request = MockMvcRequestBuilders
                .delete( "/bcs/category/delete" )
                .accept( MediaType.APPLICATION_JSON_VALUE )
                .contentType( MediaType.APPLICATION_JSON_VALUE )
                .characterEncoding( "UTF-8" )
                .content( JSON.toJSONString( delReq ) );

        try {
            mockMvc.perform( request )
                    .andExpect( handler().handlerType( CategoryController.class ) )
                    .andReturn().getResponse().getContentAsString();
        }
        catch ( Exception e ) {
            assertThat( e.getCause() ).isInstanceOf( BizException.class );
        }

        categoryMapper.deleteByPrimaryKey( categoryId );
        categoryMapper.deleteByPrimaryKey( firstChildCategoryId );
        categoryMapper.deleteByPrimaryKey( secondChildCategoryId );
    }


    public Long insert( BaseReq req ) throws Exception {
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post( "/bcs/category/upsert" )
                .accept( MediaType.APPLICATION_JSON_VALUE )
                .contentType( MediaType.APPLICATION_JSON_VALUE )
                .characterEncoding( "UTF-8" )
                .content( JSON.toJSONString( req ) );

        String respStr = mockMvc.perform( request )
                .andExpect( MockMvcResultMatchers.status().isOk() )
                .andExpect( handler().handlerType( CategoryController.class ) )
                .andReturn().getResponse().getContentAsString();
        UpsertCategoryResp resp = JSON.parseObject( respStr, UpsertCategoryResp.class );
        return resp.getCategoryId();
    }
}
