package org.interview.bcs.domains.category.cache;

import org.springframework.context.ApplicationEvent;

/**
 * @author: wanghu
 * @since: 2025/5/16 17:23
 */
public class CategoryChangedEvent extends ApplicationEvent {

    private static final long serialVersionUID = -2071562442384570521L;

    private static final Object source = new Object();

    public CategoryChangedEvent() {
        super( source );
    }
}
