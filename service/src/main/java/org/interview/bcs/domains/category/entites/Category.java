package org.interview.bcs.domains.category.entites;

import lombok.*;
import org.interview.bcs.api.base.BaseEntity;

/**
 * category
 * @author 
 */
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Category extends BaseEntity {

    private static final long serialVersionUID = -8315505224588872346L;

    /**
     * 分类 id
     */
    private Long categoryId;

    /**
     * 分类名称
     */
    private String name;

    /**
     * pid
     */
    private Long parentId;

    /**
     * 分类描述
     */
    private String description;

}