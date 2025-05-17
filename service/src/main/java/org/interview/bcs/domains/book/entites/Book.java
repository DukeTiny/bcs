package org.interview.bcs.domains.book.entites;

import lombok.*;
import org.interview.bcs.api.base.BaseEntity;

/**
 * book
 * @author 
 */
@Setter
@Getter
@NoArgsConstructor
public class Book extends BaseEntity {


    private static final long serialVersionUID = -6124552709557782155L;

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
    private Integer status;

    /**
     * 描述
     */
    private String description;

}