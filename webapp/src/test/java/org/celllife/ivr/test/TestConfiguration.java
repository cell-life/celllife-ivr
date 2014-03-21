package org.celllife.ivr.test;

import org.springframework.test.context.ContextConfiguration;
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
        "classpath:/META-INF/spring-integration/spring-integration-core.xml",
        "classpath:/META-INF/spring-integration/spring-integration-verboice.xml",
        "classpath:/META-INF/spring/spring-jdbc-verboice.xml",
        "classpath:/META-INF/spring/spring-orm-verboice.xml",
        "classpath:/META-INF/spring/spring-tx-verboice.xml",
        "classpath:/META-INF/spring-data/spring-data-jpa-verboice.xml"
})
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
public class TestConfiguration {

}
