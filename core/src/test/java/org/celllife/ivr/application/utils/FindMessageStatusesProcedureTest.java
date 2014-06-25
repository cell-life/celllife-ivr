package org.celllife.ivr.application.utils;

import org.celllife.ivr.test.TestConfiguration;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.Map;

@RunWith(SpringJUnit4ClassRunner.class)
public class FindMessageStatusesProcedureTest extends TestConfiguration {

    @Autowired
    FindMessageStatusesProcedure sproc;

    @Ignore
    @Test
    public void testReportService() {

        Map<String, Object> parameters = new HashMap<String, Object>();
        Map<String, Object> results = sproc.execute(parameters);
        sproc.printMap(results);

    }

}
