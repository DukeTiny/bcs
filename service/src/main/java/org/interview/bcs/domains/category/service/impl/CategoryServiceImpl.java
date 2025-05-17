package org.interview.bcs.domains.category.service.impl;

import com.google.common.collect.Lists;
import org.interview.bcs.api.base.BaseResp;
import org.interview.bcs.api.domains.category.req.DeleteCategoryReq;
import org.interview.bcs.api.domains.category.req.GetCategoryReq;
import org.interview.bcs.api.domains.category.req.UpsertCategoryReq;
import org.interview.bcs.api.domains.category.resp.GetCategoryResp;
import org.interview.bcs.api.domains.category.resp.UpsertCategoryResp;
import org.interview.bcs.api.domains.category.vo.CategoryVo;
import org.interview.bcs.common.BeanCopy;
import org.interview.bcs.common.BizException;
import org.interview.bcs.common.ValidationUtils;
import org.interview.bcs.domains.category.cache.CategoryCache;
import org.interview.bcs.domains.category.cache.CategoryChangedEvent;
import org.interview.bcs.domains.category.entites.Category;
import org.interview.bcs.domains.category.mappers.CategoryMapper;
import org.interview.bcs.domains.category.service.CategoryService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

import static org.interview.bcs.api.constant.Constants.ROOT_CATEGORY_ID;

/**
 * @author: wanghu
 * @since: 2025/5/16 15:57
 */
@Service
public class CategoryServiceImpl implements CategoryService {


    @Resource
    private CategoryMapper categoryMapper;

    @Resource
    private ApplicationEventPublisher applicationEventPublisher;


    @Override
    public GetCategoryResp getCategories( GetCategoryReq req ) {
        // 可以切面完成
        ValidationUtils.validate( req );

        Long categoryId = req.getCategoryId();

        // 查找某个分类及子分类
        if ( Objects.nonNull( categoryId ) ) {
            GetCategoryResp resp = new GetCategoryResp();
            resp.setCategories( new ArrayList<>() );
            Category category = CategoryCache.getCategory( categoryId );
            if ( Objects.nonNull( category ) ) {
                CategoryVo categoryVo = categoryToCategoryVo( category );
                resp.setCategories( Lists.newArrayList( categoryVo ) );
                fillChildrenCategories( resp.getCategories() );
            }
            return resp;
        }
        // 获取所有分裂
        return getAllCategories();
    }

    @Override
    public UpsertCategoryResp upsertCategory( UpsertCategoryReq req ) {
        // 可以切面
        ValidationUtils.validate( req );

        // 业务相关校验
        bizValidate( req );

        Long categoryId = req.getCategoryId();
        Category category = new Category();
        BeanCopy.copy( req, category );
        category.setName( req.getCategoryName() );

        if ( Objects.nonNull( categoryId ) ) {  // update
            categoryMapper.updateByPrimaryKeySelective( category );
        }
        else { // insert
            categoryMapper.insertSelective( category );
            categoryId = category.getCategoryId();
        }

        /*
         * 发送分类信息变更事件，让缓存强制更新，在微服务中强制更新缓存可以通过 mq 消息
         */
        applicationEventPublisher.publishEvent( new CategoryChangedEvent() );
        return UpsertCategoryResp.builder().categoryId( categoryId ).build();
    }

    private void bizValidate( UpsertCategoryReq req ) {
        final Long categoryId = req.getCategoryId(), parentId = req.getParentId();
        if ( Objects.nonNull( categoryId ) ) {  // update
            Category category = categoryMapper.selectByPrimaryKey( categoryId );
            if ( Objects.isNull( category ) ) {
                throw new BizException( String.format( "Category[%d] not found.", categoryId ) );
            }
        }

        if ( !ROOT_CATEGORY_ID.equals( parentId ) ) {
            Category parentCategory = categoryMapper.selectByPrimaryKey( parentId );
            if ( Objects.isNull( parentCategory ) ) {
                throw new BizException( String.format( "Parent category[%d] not found.", parentId ) );
            }
        }
    }

    @Override
    public BaseResp deleteCategory( DeleteCategoryReq req ) {
        // 可以切面
        ValidationUtils.validate( req );

        final Long categoryId = req.getCategoryId();

        // 索引失效，删除当前的时候，也要删除子分类
        int effectRows = categoryMapper.loginDelete( categoryId );
        if ( effectRows < 1 ) {
            throw new BizException( String.format( "Category[%d] not exists.", categoryId ) );
        }

        /*
         * 发送分类信息变更事件，让缓存强制更新，在微服务中强制更新缓存可以通过 mq 消息
         */
        applicationEventPublisher.publishEvent( new CategoryChangedEvent() );
        return BaseResp.success();
    }

    private GetCategoryResp getAllCategories() {
        GetCategoryResp resp = new GetCategoryResp();
        resp.setCategories( new ArrayList<>() );
        // 所有分类和子分类
        List<Category> categories = CategoryCache.getCategories();
        if ( !CollectionUtils.isEmpty( categories ) ) {
            // parentId 是 0L 的时候，代表是 root 分类
            List<CategoryVo> rootCategories = categories.stream()
                    .filter( c -> c.getParentId().equals( ROOT_CATEGORY_ID ) ).map( this::categoryToCategoryVo ).collect( Collectors.toList() );

            resp.setCategories( rootCategories );
            fillChildrenCategories( rootCategories );
        }
        return resp;
    }

    private void fillChildrenCategories( List<CategoryVo> rootCategories ) {
        if ( CollectionUtils.isEmpty( rootCategories ) ) {
            return;
        }
        // 利用队列，进行全部分类的数据转换
        Queue<CategoryVo> queue = new LinkedList<>( rootCategories );
        while ( !queue.isEmpty() ) {
            CategoryVo cur = queue.poll();
            List<Category> childrenCategories = CategoryCache.getChildrenCategories( cur.getCategoryId() );
            childrenCategories.forEach( child -> {
                CategoryVo categoryVo = categoryToCategoryVo( child );
                queue.add( categoryVo );
                cur.getChildren().add( categoryVo );
            } );
        }
    }


    private CategoryVo categoryToCategoryVo( Category category ) {
        CategoryVo categoryVo = new CategoryVo();
        BeanCopy.copy( category, categoryVo );
        categoryVo.setCategoryName( category.getName() );
        categoryVo.setChildren( new ArrayList<>() );
        return categoryVo;
    }

}
