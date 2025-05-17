package org.interview.bcs.domains.category;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.assertj.core.util.Lists;
import org.interview.bcs.BaseTest;
import org.interview.bcs.domains.category.entites.Category;
import org.interview.bcs.domains.category.mappers.CategoryMapper;
import org.junit.jupiter.api.Test;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.interview.bcs.domains.data.Initializer.CATEGORIES;

/**
 * @author: wanghu
 * @since: 2025/5/17 09:45
 */
@Slf4j
public class CategoryMapperTest extends BaseTest {

    @Resource
    private CategoryMapper categoryMapper;

    @Test
    void testSelectAll() {
        List<Category> categories = categoryMapper.selectAll();

        Map<Long, Category> categoryMap = categories.stream().collect( Collectors.toMap( Category::getCategoryId, category -> category ) );
        CATEGORIES.forEach( category -> {
            Category c = categoryMap.get( category.getCategoryId() );
            assertThat( c ).isNotNull();
            assertThat( c.getName() ).isEqualTo( category.getName() );
        } );

    }

    @Test
    void testLogicDelete() {
        Pair<Category, List<Category>> categoryListPair = genCategories();
        Category testRootCategory = categoryListPair.getLeft();
        List<Category> totalTestCategories = categoryListPair.getRight();

        Map<Long, Category> categoryMap = categoryMapper.selectAll().stream().collect( Collectors.toMap( Category::getCategoryId, category -> category ) );

        for ( Category testCategory : totalTestCategories ) {
            assertThat( categoryMap.get( testCategory.getCategoryId() ) ).isNotNull();
            assertThat( categoryMap.get( testCategory.getParentId() ) ).isNotNull();
        }


        categoryMapper.loginDelete( testRootCategory.getCategoryId() );

        categoryMap = categoryMapper.selectAll().stream().collect( Collectors.toMap( Category::getCategoryId, category -> category ) );
        for ( Category testCategory : totalTestCategories ) {
            assertThat( categoryMap.get( testCategory.getCategoryId() ) ).isNull();
        }

    }

    Pair<Category,List<Category>> genCategories() {

        List<Category> testCategories = Lists.newArrayList();
        Category rootCategory = Category.builder()
                .parentId( CATEGORIES.get( random.nextInt( CATEGORIES.size() ) ).getCategoryId() )
                .name( "test root" )
                .description( "test root description." )
                .build();

        categoryMapper.insertSelective( rootCategory );

        Category firstChild = Category.builder()
                .parentId( rootCategory.getCategoryId() )
                .name( "test first child" )
                .description( "test first child description." )
                .build();

        Category secondChild = Category.builder()
                .parentId( rootCategory.getCategoryId() )
                .name( "test second child" )
                .description( "test second child description." )
                .build();


        categoryMapper.insertSelective( firstChild );
        categoryMapper.insertSelective( secondChild );

        testCategories.add( rootCategory );
        testCategories.add( firstChild );
        testCategories.add( secondChild );
        return Pair.of( rootCategory, testCategories );
    }

}
