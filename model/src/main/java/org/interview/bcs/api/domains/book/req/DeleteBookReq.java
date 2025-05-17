package org.interview.bcs.api.domains.book.req;

import lombok.Getter;
import lombok.Setter;
import org.interview.bcs.api.base.BaseReq;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author: wanghu
 * @since: 2025/5/16 20:25
 */
@Setter
@Getter
public class DeleteBookReq extends BaseReq {

    private static final long serialVersionUID = 3221180037505322165L;

    /**
     * 图书 id
     */
    @NotNull
    @Min( 1L )
    private Long bookId;


}
