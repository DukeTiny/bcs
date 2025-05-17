package org.interview.bcs;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author: wanghu
 * @since: 2025/5/15 08:47
 */
@SpringBootApplication
@EnableFeignClients
@MapperScan("org.interview.bcs.domains.*.mappers")
@EnableScheduling
public class BcsApplication {

    public static void main( String[] args ) {
        SpringApplication.run( BcsApplication.class, args );
    }

}
