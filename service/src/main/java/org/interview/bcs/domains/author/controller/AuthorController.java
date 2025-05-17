package org.interview.bcs.domains.author.controller;

import org.interview.bcs.api.base.BaseResp;
import org.interview.bcs.api.domains.author.req.DeleteAuthorReq;
import org.interview.bcs.api.domains.author.req.GetAuthorReq;
import org.interview.bcs.api.domains.author.req.UpsertAuthorReq;
import org.interview.bcs.api.domains.author.resp.GetAuthorResp;
import org.interview.bcs.api.domains.author.resp.UpsertAuthorResp;
import org.interview.bcs.domains.author.service.AuthorService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author: wanghu
 * @since: 2025/5/15 19:20
 */
@RestController
@RequestMapping("/bcs")
public class AuthorController {

    @Resource
    private AuthorService authorService;

    /**
     * 查询作家
     */
    @GetMapping("/author/get")
    public GetAuthorResp getAuthors( @RequestParam(required = false) Long authorId, @RequestParam(required = false) String authorName,
                                     @RequestParam Integer pageCurrent, @RequestParam Integer pageSize ) {
        GetAuthorReq req = new GetAuthorReq();
        req.setAuthorId( authorId );
        req.setAuthorName( authorName );
        req.setPageCurrent( pageCurrent );
        req.setPageSize( pageSize );
        return authorService.getAuthors( req );
    }

    /**
     * 增、改作家信息
     */
    @PostMapping("/author/upsert")
    public UpsertAuthorResp upsertAuthor( @RequestBody UpsertAuthorReq upsertAuthorReq ) {
        return authorService.upsertAuthor( upsertAuthorReq );
    }

    /**
     * 删除作家
     */
    @DeleteMapping("/author/delete")
    public BaseResp deleteAuthor( @RequestBody DeleteAuthorReq deleteAuthorReq ) {
        return authorService.deleteAuthor( deleteAuthorReq );
    }

}
