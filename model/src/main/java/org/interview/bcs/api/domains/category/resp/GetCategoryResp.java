package org.interview.bcs.api.domains.category.resp;

import lombok.Getter;
import lombok.Setter;
import org.interview.bcs.api.base.BaseResp;
import org.interview.bcs.api.domains.category.vo.CategoryVo;

import java.util.List;

/**
 * @author: wanghu
 * @since: 2025/5/15 17:28
 */
@Setter
@Getter
public class GetCategoryResp extends BaseResp {

    private static final long serialVersionUID = -7152313119880244512L;

    /**
     * 分类
     */
    private List<CategoryVo> categories;

}
