package org.interview.bcs.api.base;


import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author: wanghu
 * @since: 2025/5/15 17:28
 */
@Setter
@Getter
public class BasePageReq extends BaseReq {

    private static final long serialVersionUID = -5750816307008286487L;

    @NotNull(message = "pageCurrent can't be null.")
    @Min(value = 1, message = "pageCurrent should gte 1.")
    private Integer pageCurrent = 1;

    @NotNull(message = "pageSize can't be null.")
    @Min(value = 1, message = "pageSize should gte 1.")
    private Integer pageSize = 10;

}
