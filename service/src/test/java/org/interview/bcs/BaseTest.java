package org.interview.bcs;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.security.SecureRandom;
import java.util.Random;

/**
 * @author: wanghu
 * @since: 2025/5/16 23:14
 */
@SpringBootTest(classes = {BcsApplication.class})
public class BaseTest {

    protected Random random = new SecureRandom();

    @Test
    void t() throws Exception{

    }

}
