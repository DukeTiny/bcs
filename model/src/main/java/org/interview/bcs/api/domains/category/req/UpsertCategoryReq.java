package org.interview.bcs.api.domains.category.req;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.interview.bcs.api.base.BaseReq;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import static org.interview.bcs.api.constant.Constants.ROOT_CATEGORY_ID;

/**
 * @author: wanghu
 * @since: 2025/5/16 16:55
 */
@Setter
@Getter
public class UpsertCategoryReq extends BaseReq {


    private static final long serialVersionUID = -3734534770523111947L;

    /**
     * 分类 id
     */
    private Long categoryId;

    /**
     * 分类名称
     */
    @NotBlank
    @Length(min = 1, max = 50, message = "categoryName's length should between 1 and 50")
    private String categoryName;

    /**
     * 父 id
     */
    @NotNull
    @Min(0L)
    private Long parentId = ROOT_CATEGORY_ID;

    /**
     * 分类描述
     */
    private String description;


}
