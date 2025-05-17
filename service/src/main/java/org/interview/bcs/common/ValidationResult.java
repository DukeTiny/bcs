package org.interview.bcs.common;

import com.alibaba.fastjson2.JSON;
import lombok.Getter;
import lombok.Setter;
import org.interview.bcs.api.base.BaseBean;

import java.util.Map;

/**
 * @author : wanghu
 * @since: 2025/5/15 18:26
 */
@Setter
@Getter
public class ValidationResult extends BaseBean {

    private static final long serialVersionUID = -8845771447271475410L;

    private boolean hasErrors;

    private Map<String, String> errorMsg;

    public String getErrorMessage() {
        return JSON.toJSONString( errorMsg );
    }

}