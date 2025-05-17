package org.interview.bcs.api.domains.author.req;

import lombok.*;
import org.interview.bcs.api.base.BasePageReq;

/**
 * @author: wanghu
 * @since: 2025/5/15 17:28
 */
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GetAuthorReq extends BasePageReq {

    private static final long serialVersionUID = -7152313119880244512L;

    /**
     * 作家 id
     */
    private Long authorId;

    /**
     * 作家名称
     */
    private String authorName;


}
