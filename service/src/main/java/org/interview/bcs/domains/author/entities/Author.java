package org.interview.bcs.domains.author.entities;

import lombok.*;
import org.interview.bcs.api.base.BaseEntity;

import java.time.LocalDate;

/**
 * authors
 */
@Setter
@Getter
@NoArgsConstructor
public class Author extends BaseEntity {

    private static final long serialVersionUID = -8681119158311044144L;

    /**
     * 作家 id
     */
    private Long authorId;

    /**
     * 作家名称
     */
    private String name;

    /**
     * 国籍
     */
    private String nationality;

    private LocalDate birthDate;

    private LocalDate deathDate;


}