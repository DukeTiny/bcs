package org.interview.bcs.api.domains.category.vo;

import lombok.Getter;
import lombok.Setter;
import org.interview.bcs.api.base.BaseBean;

import java.util.List;

/**
 * @author: wanghu
 * @since: 2025/5/16 16:00
 */
@Setter
@Getter
public class CategoryVo extends BaseBean {

    private static final long serialVersionUID = -6702155376644910356L;


    /**
     * 分类 id
     */
    private Long categoryId;

    /**
     * 分类名称
     */
    private String categoryName;

    /**
     * 描述
     */
    private String description;

    /**
     * 子分类
     */
    private List<CategoryVo> children;

}
