package org.interview.bcs.api.domains.book.req;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.interview.bcs.api.base.BaseReq;
import org.interview.bcs.api.constant.BookStatusEnum;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author: wanghu
 * @since: 2025/5/16 20:25
 */
@Setter
@Getter
public class UpsertBookReq extends BaseReq {

    private static final long serialVersionUID = 3221180037505322165L;


    /**
     * 主键 id
     */
    private Long bookId;

    /**
     * isbn
     */
    @NotBlank
    @Length(min = 1, max = 50)
    private String isbn;

    /**
     * 书籍名称
     */
    @NotBlank
    @Length(min = 1, max = 255)
    private String title;

    /**
     * 作者 id
     */
    @NotNull
    @Min(1L)
    private Long authorId;

    /**
     * 出版商 id
     */
    @NotNull
    @Min(1L)
    private Long publisherId;

    /**
     * 出版年
     */
    @NotNull
    @Min( 1L )
    private Integer publicationYear;

    /**
     * 分类 id
     */
    @NotNull
    private Long categoryId;

    /**
     * 书籍语言
     */
    @NotBlank
    @Length(min = 1, max = 30)
    private String language;

    private Integer pageCount;

    private String coverImage;

    /**
     * -10:taken_off 0:available, 10:checked_out, 20:lost, 30:removed
     */
    @NotNull
    private BookStatusEnum bookStatus;

    /**
     * 描述
     */
    private String description;


}
