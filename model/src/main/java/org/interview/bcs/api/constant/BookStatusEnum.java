package org.interview.bcs.api.constant;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * @author: wanghu
 * @since: 2025/5/16 20:43
 */
@Getter
public enum BookStatusEnum {

    TAKEN_OFF( -10, "taken_off" ),

    AVAILABLE( 0, "available" ),

    CHECKED_OUT( 10, "checked_out" ),

    LOST( 20, "lost" ),

    REMOVED( 30, "removed" );

    @JsonValue
    private final int code;

    private final String status;

    BookStatusEnum( int code, String status ) {
        this.code = code;
        this.status = status;
    }

    public static BookStatusEnum of( int code ) {
        for ( BookStatusEnum cur : values() ) {
            if ( cur.code == code ) {
                return cur;
            }
        }
        throw new IllegalArgumentException( "Invalid book status code: " + code );
    }

}
