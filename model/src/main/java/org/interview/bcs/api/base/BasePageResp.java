package org.interview.bcs.api.base;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.function.Consumer;

/**
 * @author: wanghu
 * @since: 2025/5/15 17:34
 */
@Setter
@Getter
public class BasePageResp<T> extends BaseResp {

    private static final long serialVersionUID = -981877258514054189L;

    private long totals;

    private long pages;

    public void convert( PageVo<T> pageVo, Consumer<List<T>> consumer ) {
        this.pages = pageVo.getPages();
        this.totals = pageVo.getTotals();
        consumer.accept( pageVo.getRows() );
    }

}
