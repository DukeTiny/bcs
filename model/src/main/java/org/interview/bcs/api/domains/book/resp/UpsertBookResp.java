package org.interview.bcs.api.domains.book.resp;

import lombok.*;
import org.interview.bcs.api.base.BaseResp;

/**
 * @author: wanghu
 * @since: 2025/5/16 20:25
 */
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpsertBookResp extends BaseResp {

    private static final long serialVersionUID = 3221180037505322165L;


    /**
     * 主键 id
     */
    private Long bookId;

}
