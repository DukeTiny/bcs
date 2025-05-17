package org.interview.bcs.api.domains.author.resp;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.interview.bcs.api.base.BaseResp;

/**
 * @author: wanghu
 * @since: 2025/5/16 12:11
 */
@Setter
@Getter
@Builder
public class UpsertAuthorResp extends BaseResp {

    private static final long serialVersionUID = 5189609034128995220L;

    private Long authorId;


}
