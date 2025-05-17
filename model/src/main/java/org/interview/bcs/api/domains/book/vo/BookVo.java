package org.interview.bcs.api.domains.book.vo;

import lombok.Getter;
import lombok.Setter;
import org.interview.bcs.api.base.BaseBean;
import org.interview.bcs.api.constant.BookStatusEnum;

/**
 * @author: wanghu
 * @since: 2025/5/15 09:57
 */
@Setter
@Getter
public class BookVo extends BaseBean {

    private static final long serialVersionUID = -5182253163156550692L;


    /**
     * 主键 id
     */
    private Long bookId;

    /**
     * isbn
     */
    private String isbn;

    /**
     * 书籍名称
     */
    private String title;

    /**
     * 作者 id
     */
    private Long authorId;

    /**
     * 出版商 id
     */
    private Long publisherId;

    /**
     * 出版年
     */
    private Integer publicationYear;

    /**
     * 分类 id
     */
    private Long categoryId;

    /**
     * 书籍语言
     */
    private String language;

    private Integer pageCount;

    private String coverImage;

    /**
     * -10:taken_off 0:available, 10:checked_out, 20:lost, 30:removed
     */
    private BookStatusEnum bookStatus;

    /**
     * 描述
     */
    private String description;

}
