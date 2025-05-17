package org.interview.bcs.domains.category;

import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.interview.bcs.BaseTest;
import org.interview.bcs.api.constant.Constants;
import org.interview.bcs.api.domains.category.req.UpsertCategoryReq;
import org.interview.bcs.api.domains.category.resp.UpsertCategoryResp;
import org.interview.bcs.common.BizException;
import org.interview.bcs.domains.category.cache.CategoryChangedEvent;
import org.interview.bcs.domains.category.controller.CategoryController;
import org.interview.bcs.domains.category.entites.Category;
import org.interview.bcs.domains.category.mappers.CategoryMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.MediaType;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import javax.annotation.Resource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.interview.bcs.domains.data.Initializer.CATEGORIES;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.handler;

/**
 * @author: wanghu
 * @since: 2025/5/17 10:43
 */
@Slf4j
@WebAppConfiguration
public class UpsertCategoryControllerTest extends BaseTest {

    @Resource
    private CategoryController categoryController;

    @Resource
    private CategoryMapper categoryMapper;

    private MockMvc mockMvc;

    @Resource
    private ApplicationEventPublisher applicationEventPublisher;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup( categoryController ).build();
    }

    @Test
    void testUpsertCategory() throws Exception {
        testUpsert();
        testCategoryNotExists();
        testParentCategoryNotExists();
    }

    private void testCategoryNotExists() throws Exception {

        UpsertCategoryReq req = new UpsertCategoryReq();
        req.setCategoryName( "Category:Test4" );
        req.setParentId( Constants.ROOT_CATEGORY_ID );
        req.setDescription( "Category:Description" );
        req.setCategoryId( Long.MAX_VALUE );

        // 插入测试
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post( "/bcs/category/upsert" )
                .accept( MediaType.APPLICATION_JSON_VALUE )
                .contentType( MediaType.APPLICATION_JSON_VALUE )
                .characterEncoding( "UTF-8" )
                .content( JSON.toJSONString( req ) );

        try {
            mockMvc.perform( request )
                    .andExpect( MockMvcResultMatchers.status().isOk() )
                    .andExpect( handler().handlerType( CategoryController.class ) )
                    .andReturn().getResponse().getContentAsString();
        }
        catch ( Exception e ) {
            assertThat( e.getCause() ).isInstanceOf( BizException.class );
        }

    }

    private void testParentCategoryNotExists() throws Exception {

        UpsertCategoryReq req = new UpsertCategoryReq();
        req.setCategoryName( "Category:Test3" );
        req.setParentId( Long.MAX_VALUE );
        req.setDescription( "Category:Description" );

        // 插入测试
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .post( "/bcs/category/upsert" )
                .accept( MediaType.APPLICATION_JSON_VALUE )
                .contentType( MediaType.APPLICATION_JSON_VALUE )
                .characterEncoding( "UTF-8" )
                .content( JSON.toJSONString( req ) );

        try {
            mockMvc.perform( request )
                    .andExpect( MockMvcResultMatchers.status().isOk() )
                    .andExpect( handler().handlerType( CategoryController.class ) )
                    .andReturn().getResponse().getContentAsString();
        }
        catch ( Exception e ) {
            assertThat( e.getCause() ).isInstanceOf( BizException.class );
        }

    }

    private void testUpsert() throws Exception {
        UpsertCategoryReq req = new UpsertCategoryReq();
        req.setCategoryName( "Category:Test2" );
        req.setParentId( Constants.ROOT_CATEGORY_ID );
        req.setDescription( "Category:Description" );

        // 插入测试
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

        final Long categoryId = resp.getCategoryId();
        Category category = categoryMapper.selectByPrimaryKey( categoryId );
        assertThat( category ).isNotNull();
        assertThat( category.getName() ).isEqualTo( req.getCategoryName() );
        assertThat( category.getDescription() ).isEqualTo( req.getDescription() );
        assertThat( category.getParentId() ).isEqualTo( req.getParentId() );


        // 更新测试
        req.setCategoryId( categoryId );
        req.setCategoryName( "Category:Test:Update" );
        req.setParentId( CATEGORIES.get( random.nextInt( CATEGORIES.size() ) ).getCategoryId() );
        req.setDescription( "Category:Description:Update" );

        request = MockMvcRequestBuilders
                .post( "/bcs/category/upsert" )
                .accept( MediaType.APPLICATION_JSON_VALUE )
                .contentType( MediaType.APPLICATION_JSON_VALUE )
                .characterEncoding( "UTF-8" )
                .content( JSON.toJSONString( req ) );

        mockMvc.perform( request )
                .andExpect( MockMvcResultMatchers.status().isOk() )
                .andExpect( handler().handlerType( CategoryController.class ) )
                .andReturn().getResponse().getContentAsString();

        category = categoryMapper.selectByPrimaryKey( categoryId );
        assertThat( category.getName() ).isEqualTo( req.getCategoryName() );
        assertThat( category.getDescription() ).isEqualTo( req.getDescription() );
        assertThat( category.getParentId() ).isEqualTo( req.getParentId() );

        categoryMapper.deleteByPrimaryKey( categoryId );
        category = categoryMapper.selectByPrimaryKey( categoryId );
        assertThat( category ).isNull();
        applicationEventPublisher.publishEvent( new CategoryChangedEvent() );
    }


}
