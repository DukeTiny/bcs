package org.interview.bcs.api.domains.book.resp;

import lombok.Getter;
import lombok.Setter;
import org.interview.bcs.api.base.BasePageResp;
import org.interview.bcs.api.domains.book.vo.BookVo;

import java.util.ArrayList;
import java.util.List;

/**
 * @author: wanghu
 * @since: 2025/5/16 20:25
 */
@Setter
@Getter
public class GetBookResp extends BasePageResp<BookVo> {

    private static final long serialVersionUID = 3221180037505322165L;

    private List<BookVo> books = new ArrayList<>();


}
