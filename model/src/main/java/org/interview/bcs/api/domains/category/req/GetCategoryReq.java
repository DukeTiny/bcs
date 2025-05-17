package org.interview.bcs.api.domains.category.req;

import lombok.*;
import org.interview.bcs.api.base.BaseReq;

/**
 * @author: wanghu
 * @since: 2025/5/15 17:28
 */
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetCategoryReq extends BaseReq {

    private static final long serialVersionUID = -7152313119880244512L;

    /**
     * 分类 id
     */
    private Long categoryId;


}
