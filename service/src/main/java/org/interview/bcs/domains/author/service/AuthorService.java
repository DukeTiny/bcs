package org.interview.bcs.domains.author.service;

import org.interview.bcs.api.base.BaseResp;
import org.interview.bcs.api.domains.author.req.DeleteAuthorReq;
import org.interview.bcs.api.domains.author.req.GetAuthorReq;
import org.interview.bcs.api.domains.author.req.UpsertAuthorReq;
import org.interview.bcs.api.domains.author.resp.GetAuthorResp;
import org.interview.bcs.api.domains.author.resp.UpsertAuthorResp;

/**
 * @author: wanghu
 * @since: 2025/5/15 14:43
 */
public interface AuthorService {

    /**
     * 分页获取作家列表
     */
    GetAuthorResp getAuthors( GetAuthorReq getAuthorReq );

    /**
     * 增、改作家信息
     */
    UpsertAuthorResp upsertAuthor( UpsertAuthorReq upsertAuthorReq );

    /**
     * 删除作家
     */
    BaseResp deleteAuthor( DeleteAuthorReq deleteAuthorReq );

}
