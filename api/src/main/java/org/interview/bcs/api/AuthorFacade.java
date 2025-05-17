package org.interview.bcs.api;

import org.interview.bcs.api.base.BaseResp;
import org.interview.bcs.api.domains.author.req.DeleteAuthorReq;
import org.interview.bcs.api.domains.author.req.UpsertAuthorReq;
import org.interview.bcs.api.domains.author.resp.GetAuthorResp;
import org.interview.bcs.api.domains.author.resp.UpsertAuthorResp;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author: wanghu
 * @since: 2025/5/15 19:20
 */
@FeignClient(value = "authorFacade", path = "/bcs")
public interface AuthorFacade {

    /**
     * 查询作家
     */
    @GetMapping("/author/get")
    GetAuthorResp getAuthors( Long authorId, String authorName, Integer pageCurrent, Integer pageSize );

    /**
     * 增、改作家信息
     */
    @PostMapping("/author/upsert")
    UpsertAuthorResp upsertAuthor( @RequestBody UpsertAuthorReq upsertAuthorReq );

    /**
     * 删除作家
     */
    @DeleteMapping("/author/delete")
    BaseResp deleteAuthor( @RequestBody DeleteAuthorReq deleteAuthorReq );

}
