package org.interview.bcs.api.domains.category.req;

import lombok.Getter;
import lombok.Setter;
import org.interview.bcs.api.base.BaseReq;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author: wanghu
 * @since: 2025/5/16 16:55
 */
@Setter
@Getter
public class DeleteCategoryReq extends BaseReq {


    private static final long serialVersionUID = -3734534770523111947L;

    /**
     * 分类 id
     */
    @NotNull
    @Min( 1L )
    private Long categoryId;


}
