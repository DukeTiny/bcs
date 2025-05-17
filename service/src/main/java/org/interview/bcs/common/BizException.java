package org.interview.bcs.common;

import lombok.Getter;
import lombok.Setter;

/**
 * @author : wanghu
 * @since: 2025/5/15 18:26
 */
@Getter
@Setter
public class BizException extends RuntimeException {

    private static final long serialVersionUID = -1498915060516260803L;

    private String code;

    public BizException( String message ) {
        super( message );
    }

}
