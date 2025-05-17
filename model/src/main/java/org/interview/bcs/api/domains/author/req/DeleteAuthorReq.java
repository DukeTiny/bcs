package org.interview.bcs.api.domains.author.req;

import lombok.Getter;
import lombok.Setter;
import org.interview.bcs.api.base.BaseReq;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author: wanghu
 * @since: 2025/5/16 12:11
 */
@Setter
@Getter
public class DeleteAuthorReq extends BaseReq {

    private static final long serialVersionUID = 5189609034128995220L;

    /**
     * 作家 id
     */
    @NotNull
    @Min( value = 1L,message = "author's id can't be null.")
    private Long authorId;


}
