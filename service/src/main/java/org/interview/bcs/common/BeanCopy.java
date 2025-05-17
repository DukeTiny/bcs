package org.interview.bcs.common;

import org.springframework.cglib.beans.BeanCopier;

import java.util.Objects;

/**
 * @author: wanghu
 * @since: 2025/5/15 18:26
 */
public class BeanCopy {

    public static void copy( Object source, Object target ) {
        Objects.requireNonNull( source );
        Objects.requireNonNull( target );
        BeanCopier.create( source.getClass(), target.getClass(), false ).copy( source, target, null );
    }

}
