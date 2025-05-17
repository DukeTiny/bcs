package org.interview.bcs.api.domains.author.resp;

import lombok.Getter;
import lombok.Setter;
import org.interview.bcs.api.base.BasePageResp;
import org.interview.bcs.api.domains.author.vo.AuthorVo;

import java.util.List;

/**
 * @author: wanghu
 * @since: 2025/5/15 17:28
 */
@Setter
@Getter
public class GetAuthorResp extends BasePageResp<AuthorVo> {

    private static final long serialVersionUID = -7152313119880244512L;

    private List<AuthorVo> authors;


}
