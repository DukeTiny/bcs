package org.interview.bcs.api.domains.author.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.interview.bcs.api.base.BaseBean;

import java.time.LocalDate;

/**
 * @author: wanghu
 * @since: 2025/5/15 17:31
 */
@Setter
@Getter
public class AuthorVo extends BaseBean {

    private static final long serialVersionUID = 9006767290570800241L;

    /**
     * 作家 id
     */
    private Long authorId;

    /**
     * 作家名称
     */
    private String authorName;

    /**
     * 国籍
     */
    private String nationality;

    /**
     * 生辰
     */
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private LocalDate birthDate;

    /**
     * 逝世时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd",timezone = "GMT+8")
    private LocalDate deathDate;


}
