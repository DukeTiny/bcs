package org.interview.bcs.domains.category;

import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.interview.bcs.BaseTest;
import org.interview.bcs.api.constant.Constants;
import org.interview.bcs.api.domains.category.resp.GetCategoryResp;
import org.interview.bcs.api.domains.category.vo.CategoryVo;
import org.interview.bcs.domains.category.controller.CategoryController;
import org.interview.bcs.domains.category.entites.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.interview.bcs.domains.data.Initializer.CATEGORIES;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.handler;

/**
 * @author: wanghu
 * @since: 2025/5/17 10:43
 */
@Slf4j
@WebAppConfiguration
public class GetCategoryControllerTest extends BaseTest {

    @Resource
    private CategoryController categoryController;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup( categoryController ).build();
    }

    @Test
    void testGetAuthors() throws Exception {
        testGetWithId();
        testGetAll();
    }

    private void testGetWithId() throws Exception {
        Category category = CATEGORIES.get( random.nextInt( CATEGORIES.size() ) );
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get( "/bcs/category/get" )
                .accept( MediaType.APPLICATION_JSON_VALUE )
                .characterEncoding( "UTF-8" )
                .param( "categoryId", category.getCategoryId().toString() );


        String respStr = mockMvc.perform( request )
                .andExpect( MockMvcResultMatchers.status().isOk() )
                .andExpect( handler().handlerType( CategoryController.class ) )
                .andReturn().getResponse().getContentAsString();

        GetCategoryResp resp = JSON.parseObject( respStr, GetCategoryResp.class );
        List<CategoryVo> categories = resp.getCategories();

        assertThat( categories ).hasSize( 1 );
        CategoryVo c = categories.get( 0 );
        assertThat( c.getCategoryId() ).isEqualTo( category.getCategoryId() );
        assertThat( c.getCategoryName() ).isEqualTo( category.getName() );
        assertThat( c.getDescription() ).isEqualTo( category.getDescription() );


    }

    private void testGetAll() throws Exception {
        MockHttpServletRequestBuilder request = MockMvcRequestBuilders
                .get( "/bcs/category/get" )
                .accept( MediaType.APPLICATION_JSON_VALUE )
                .characterEncoding( "UTF-8" );


        String respStr = mockMvc.perform( request )
                .andExpect( MockMvcResultMatchers.status().isOk() )
                .andExpect( handler().handlerType( CategoryController.class ) )
                .andReturn().getResponse().getContentAsString();

        GetCategoryResp resp = JSON.parseObject( respStr, GetCategoryResp.class );
        List<CategoryVo> categories = resp.getCategories();
        assertThat( categories ).hasSize( (int) CATEGORIES.stream().filter( e -> Constants.ROOT_CATEGORY_ID.equals( e.getParentId() ) ).count() );

        Queue<CategoryVo> queue = new LinkedList<>( categories );

        List<CategoryVo> flatList = new ArrayList<>();
        while ( !queue.isEmpty() ) {
            CategoryVo cur = queue.poll();
            if ( !CollectionUtils.isEmpty( cur.getChildren() ) ) {
                cur.getChildren().forEach( queue::offer );
            }
            flatList.add( cur );
        }

        assertThat( flatList ).hasSize( CATEGORIES.size() );

    }


}
