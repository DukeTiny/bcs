package org.interview.bcs.domains.category.components;

import lombok.extern.slf4j.Slf4j;
import org.interview.bcs.domains.category.cache.CategoryCache;
import org.springframework.context.annotation.DependsOn;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author: wanghu
 * @since: 2025/5/16 17:20
 */
@Component
@Slf4j
@DependsOn({"springUtil"})
public class CategoryScheduler {

    @PostConstruct
    public void init() {
        log.info( "Initializing categoryScheduler." );
        CategoryCache.init();
        log.info( "Initializing categoryScheduler done." );
    }

    @Scheduled(cron = "0 */1 * * * *")
    public void reload() {
        log.info( "Reload categoryCache start...." );
        CategoryCache.load();
        log.info( "Reload categoryCache end...." );
    }

}
