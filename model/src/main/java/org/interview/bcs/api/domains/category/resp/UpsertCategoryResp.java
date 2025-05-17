package org.interview.bcs.api.domains.category.resp;

import lombok.*;
import org.interview.bcs.api.base.BaseResp;

/**
 * @author: wanghu
 * @since: 2025/5/16 16:55
 */
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpsertCategoryResp extends BaseResp {


    private static final long serialVersionUID = -3734534770523111947L;

    /**
     * 分类 id
     */
    private Long categoryId;


}
