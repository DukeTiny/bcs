package org.interview.bcs.api.base;


import lombok.Getter;
import lombok.Setter;

/**
 * @author: wanghu
 * @since: 2025/5/15 17:28
 */
@Setter
@Getter
public class BaseReq extends BaseBean{

    private static final long serialVersionUID = 7157855564287841616L;

    private String requestId;


}
