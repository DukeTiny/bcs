package org.interview.bcs.api.base;

import lombok.Getter;
import lombok.Setter;
import org.interview.bcs.api.constant.RespCode;

/**
 * @author: wanghu
 * @since: 2025/5/15 17:34
 */
@Setter
@Getter
public class BaseResp extends BaseBean {

    private static final long serialVersionUID = -981877258514054189L;

    private boolean success;

    private String code;

    private String msg;

    public BaseResp() {
        this.code = RespCode.SUCCESS;
        this.msg = RespCode.SUCCESS_MSG;
    }

    public boolean isSuccess() {
        return RespCode.SUCCESS.equalsIgnoreCase( this.code );
    }

    public static BaseResp success() {
        return new BaseResp();
    }

    public static BaseResp fail( String code, String msg ) {
        BaseResp resp = new BaseResp();
        resp.setCode( code );
        resp.setMsg( msg );
        return resp;
    }



}
