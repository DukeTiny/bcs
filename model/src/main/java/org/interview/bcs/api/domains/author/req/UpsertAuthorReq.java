package org.interview.bcs.api.domains.author.req;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.interview.bcs.api.base.BaseReq;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;

/**
 * @author: wanghu
 * @since: 2025/5/16 12:11
 */
@Setter
@Getter
public class UpsertAuthorReq extends BaseReq {

    private static final long serialVersionUID = 5189609034128995220L;

    /**
     * 作家 id
     */
    private Long authorId;

    /**
     * 作家名称
     */
    @NotBlank
    @Length(min = 1, max = 100,message = "author's name can't be blank,length should between 1 and 100.")
    private String authorName;

    /**
     * 国籍
     */
    @NotBlank
    @Length(min = 1, max = 50,message = "nationality should be blank,length should between 1 and 50.")
    private String nationality;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDate birthDate;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private LocalDate deathDate;


}
