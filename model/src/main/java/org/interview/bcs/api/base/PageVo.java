package org.interview.bcs.api.base;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author: wanghu
 * @since: 2025/5/15 18:58
 */
@Setter
@Getter
public class PageVo<T> extends BaseBean{

    private static final long serialVersionUID = 6334150374365267108L;

    /**
     * 总页数
     */
    private int pages;

    /**
     * 总记录数
     */
    private long totals;

    /**
     * 页内容
     */
    private List<T> rows;

}
