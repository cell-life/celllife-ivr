package org.celllife.ivr.test;

import org.junit.runner.RunWith;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;

@ContextConfiguration({
        "classpath:/META-INF/spring/spring-aop.xml",
        "classpath:/META-INF/spring/spring-application.xml",
        "classpath:/META-INF/spring/spring-cache.xml",
        "classpath:/META-INF/spring/spring-config.xml",
        "classpath:/META-INF/spring/spring-domain.xml",
        "classpath:/META-INF/spring/spring-dozer.xml",
        "classpath:/META-INF/spring/spring-smooks.xml",
        "classpath:/META-INF/spring/spring-jdbc.xml",
        "classpath:/META-INF/spring/spring-json.xml",
        "classpath:/META-INF/spring/spring-orm.xml",
        "classpath:/META-INF/spring/spring-task.xml",
        "classpath:/META-INF/spring/spring-tx.xml",
        "classpath:/META-INF/spring-data/spring-data-jpa.xml",
        "classpath:/META-INF/spring-integration/spring-integration-core.xml"
})
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
public class TestConfiguration {

}
