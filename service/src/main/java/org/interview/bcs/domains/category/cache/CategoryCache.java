package org.interview.bcs.domains.category.cache;

import org.interview.bcs.common.SpringUtil;
import org.interview.bcs.domains.category.entites.Category;
import org.interview.bcs.domains.category.mappers.CategoryMapper;
import org.springframework.util.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author: wanghu
 * @since: 2025/5/15 17:23
 */
public final class CategoryCache {

    private static Map<Long, Category> categoryMap = new HashMap<>();

    private CategoryCache() {}

    public static void init() {
        load();
    }

    public static void load() {
        CategoryMapper mapper = SpringUtil.getBean( CategoryMapper.class );
        List<Category> categories = mapper.selectAll();
        if ( !CollectionUtils.isEmpty( categories ) ) {
            categoryMap = categories.stream().collect( Collectors.toMap( Category::getCategoryId, c -> c ) );
        }
    }

    public static Category getCategory( Long id ) {
        Objects.requireNonNull( id );
        return categoryMap.get( id );
    }

    public static List<Category> getCategories() {
        return new ArrayList<>( categoryMap.values() );
    }

    public static List<Category> getChildrenCategories( Long id ) {
        Objects.requireNonNull( id );
        List<Category> childrenCategories = new ArrayList<>();
        if ( !CollectionUtils.isEmpty( categoryMap ) ) {
            categoryMap.values().stream().filter( e -> e.getParentId().equals( id ) ).forEach( childrenCategories::add );
        }

        return childrenCategories;
    }

    private static Map<Long, Category> getAllCategories() {
        CategoryMapper mapper = SpringUtil.getBean( CategoryMapper.class );
        List<Category> categories = mapper.selectAll();
        return categories.stream().collect( Collectors.toMap( Category::getCategoryId, e -> e ) );
    }

}
