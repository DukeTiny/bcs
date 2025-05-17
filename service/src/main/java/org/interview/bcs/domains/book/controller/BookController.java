package org.interview.bcs.domains.book.controller;

import org.interview.bcs.api.base.BaseResp;
import org.interview.bcs.api.constant.BookStatusEnum;
import org.interview.bcs.api.domains.book.req.DeleteBookReq;
import org.interview.bcs.api.domains.book.req.GetBookReq;
import org.interview.bcs.api.domains.book.req.UpsertBookReq;
import org.interview.bcs.api.domains.book.resp.GetBookResp;
import org.interview.bcs.api.domains.book.resp.UpsertBookResp;
import org.interview.bcs.domains.book.service.BookService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * @author: wanghu
 * @since: 2025/5/15 09:42
 */
@RestController
@RequestMapping("/bcs")
public class BookController {

    @Resource
    private BookService bookService;

    /**
     * 查找图书
     */
    @GetMapping("/book/get")
    public GetBookResp getBooks(
            @RequestParam(required = false) String isbn, @RequestParam(required = false) String title,
            @RequestParam(required = false) String authorName, @RequestParam(required = false) Integer bookStatus,
            @RequestParam(required = false) Integer pageCurrent, @RequestParam(required = false) Integer pageSize
    ) {
        GetBookReq req = new GetBookReq();
        req.setIsbn( isbn );
        req.setTitle( title );
        req.setAuthorName( authorName );
        if ( Objects.nonNull( bookStatus ) ) {
            req.setBookStatus( BookStatusEnum.of( bookStatus ) );
        }

        if ( Objects.nonNull( pageCurrent ) ) {
            req.setPageCurrent( pageCurrent );
        }

        if ( Objects.nonNull( pageSize ) ) {
            req.setPageSize( pageSize );
        }

        return bookService.getBooks( req );
    }


    /**
     * 添加或更新图书
     */
    @PostMapping("/book/upsert")
    public UpsertBookResp upsertBook( @RequestBody UpsertBookReq req ) {
        return bookService.upsertBook( req );
    }


    /**
     * 删除图书
     */
    @DeleteMapping("/book/delete")
    public BaseResp deleteBook( @RequestBody DeleteBookReq req ) {
        return bookService.deleteBook( req );
    }

}
