package org.interview.bcs.domains.category.controller;

import org.interview.bcs.api.base.BaseResp;
import org.interview.bcs.api.domains.category.req.DeleteCategoryReq;
import org.interview.bcs.api.domains.category.req.GetCategoryReq;
import org.interview.bcs.api.domains.category.req.UpsertCategoryReq;
import org.interview.bcs.api.domains.category.resp.GetCategoryResp;
import org.interview.bcs.api.domains.category.resp.UpsertCategoryResp;
import org.interview.bcs.domains.category.service.CategoryService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author: wanghu
 * @since: 2025/5/16 19:15
 */
@RestController
@RequestMapping("/bcs")
public class CategoryController {

    @Resource
    private CategoryService categoryService;

    /**
     * 获取分类
     */
    @GetMapping("/category/get")
    public GetCategoryResp getCategories( @RequestParam(required = false) Long categoryId ) {
        GetCategoryReq req = new GetCategoryReq();
        req.setCategoryId( categoryId );
        return categoryService.getCategories( req );
    }


    /**
     * 插入或更新分类
     */
    @PostMapping("/category/upsert")
    public UpsertCategoryResp upsertCategory( @RequestBody UpsertCategoryReq upsertCategoryReq ) {
        return categoryService.upsertCategory( upsertCategoryReq );
    }


    /**
     * 删除分类
     */
    @DeleteMapping("/category/delete")
    public BaseResp deleteCategory( @RequestBody DeleteCategoryReq deleteCategoryReq ) {
        return categoryService.deleteCategory( deleteCategoryReq );
    }

}
