package org.interview.bcs.domains.book.service;

import org.interview.bcs.api.base.BaseResp;
import org.interview.bcs.api.domains.book.req.DeleteBookReq;
import org.interview.bcs.api.domains.book.req.GetBookReq;
import org.interview.bcs.api.domains.book.req.UpsertBookReq;
import org.interview.bcs.api.domains.book.resp.GetBookResp;
import org.interview.bcs.api.domains.book.resp.UpsertBookResp;

/**
 * @author: wanghu
 * @since: 2025/5/15 09:59
 */
public interface BookService {

    /**
     * 查找图书
     */
    GetBookResp getBooks( GetBookReq req );


    /**
     * 添加或更新图书
     */
    UpsertBookResp upsertBook( UpsertBookReq req );


    /**
     * 删除图书
     */
    BaseResp deleteBook( DeleteBookReq req );

}
