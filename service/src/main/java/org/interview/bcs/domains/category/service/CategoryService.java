package org.interview.bcs.domains.category.service;

import org.interview.bcs.api.base.BaseResp;
import org.interview.bcs.api.domains.category.req.DeleteCategoryReq;
import org.interview.bcs.api.domains.category.req.GetCategoryReq;
import org.interview.bcs.api.domains.category.req.UpsertCategoryReq;
import org.interview.bcs.api.domains.category.resp.GetCategoryResp;
import org.interview.bcs.api.domains.category.resp.UpsertCategoryResp;

/**
 * @author: wanghu
 * @since: 2025/5/16 15:57
 */
public interface CategoryService {

    /**
     * 获取分类
     */
    GetCategoryResp getCategories( GetCategoryReq getCategoryReq );


    /**
     * 插入或更新分类
     */
    UpsertCategoryResp upsertCategory( UpsertCategoryReq upsertCategoryReq );


    /**
     * 删除分类
     */
    BaseResp deleteCategory( DeleteCategoryReq deleteCategoryReq );

}
