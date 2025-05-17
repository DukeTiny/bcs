package org.interview.bcs.api.domains.book.req;

import lombok.Getter;
import lombok.Setter;
import org.interview.bcs.api.base.BasePageReq;
import org.interview.bcs.api.constant.BookStatusEnum;

/**
 * @author: wanghu
 * @since: 2025/5/16 20:25
 */
@Setter
@Getter
public class GetBookReq extends BasePageReq {

    private static final long serialVersionUID = 3221180037505322165L;

    /**
     * isbn
     */
    private String isbn;

    /**
     * 书记名称
     */
    private String title;

    /**
     * 作家名称
     */
    private String authorName;

    /**
     * 图书状态
     */
    private BookStatusEnum bookStatus;


}
