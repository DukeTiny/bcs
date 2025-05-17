package org.interview.bcs.api.base;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author: wanghu
 * @since: 2025/5/15 14:35
 */
@Setter
@Getter
public class BaseEntity extends BaseBean {

    private static final long serialVersionUID = 3163232427410588253L;

    /**
     * 添加时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 是否删除
     */
    private Boolean disable;

    /**
     * 备注
     */
    private String remark;

}
