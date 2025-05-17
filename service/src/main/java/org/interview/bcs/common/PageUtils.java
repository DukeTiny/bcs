package org.interview.bcs.common;

import com.github.pagehelper.PageHelper;
import org.interview.bcs.api.base.BasePageReq;
import org.interview.bcs.api.base.PageVo;

import java.io.Serializable;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author: wanghu
 * @since: 2025/5/15 18:26
 */
public final class PageUtils {


    public static <T, R extends Serializable, P extends BasePageReq> PageVo<R> query(
            final P req, final Function<P, List<T>> queryListFunc, final Function<T, R> convertFunc ) {
        PageHelper.startPage( req.getPageCurrent(), req.getPageSize() );
        List<T> list = queryListFunc.apply( req );
        PageVo<R> page = toPage( list );
        page.setRows( list.stream().map( convertFunc ).collect( Collectors.toList() ) );
        return page;
    }

    private static <T, R extends Serializable> PageVo<R> toPage( final List<T> list ) {
        PageVo<R> page = new PageVo<>();
        if ( list instanceof com.github.pagehelper.Page ) {
            com.github.pagehelper.Page<T> pageHelperPage = ( com.github.pagehelper.Page<T> ) list;
            page.setTotals( pageHelperPage.getTotal() );
            page.setPages( pageHelperPage.getPages() );
        }
        return page;
    }


}
