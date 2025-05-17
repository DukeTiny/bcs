package org.interview.bcs.api.base;

import com.alibaba.fastjson2.JSON;

import java.io.Serializable;

/**
 * @author: wanghu
 * @since: 2025/5/15 14:35
 */
public class BaseBean implements Serializable {

    private static final long serialVersionUID = 3168232427410588253L;

    @Override
    public String toString() {
        return JSON.toJSONString( this );
    }
}
