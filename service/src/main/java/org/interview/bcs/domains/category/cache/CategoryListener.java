package org.interview.bcs.domains.category.cache;

import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 * @author: wanghu
 * @since: 2025/5/16 17:23
 */
@Component
public class CategoryListener implements ApplicationListener<CategoryChangedEvent> {

    /**
     * 微服务的情况下，事件可以改为 mq 的发送
     * @param ignored 这里的 event 可以继续细化为不同的，增、删、更新，这里不需要，只要知道变了就行
     */
    @Override
    public void onApplicationEvent( CategoryChangedEvent ignored ) {
        CategoryCache.load();
    }

}
