package org.interview.bcs.api;

import org.interview.bcs.api.domains.book.vo.BookVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

/**
 * @author: wanghu
 * @since: 2025/5/15 08:39
 */
@FeignClient(value = "bookFacade",path = "/bcs/book")
public interface BookFacade {

    @GetMapping("/list")
    List<BookVo> getAllBooks();

}
