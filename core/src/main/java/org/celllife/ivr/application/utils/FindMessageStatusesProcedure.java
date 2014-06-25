package org.celllife.ivr.application.utils;

import org.springframework.jdbc.object.StoredProcedure;

import javax.sql.DataSource;
import java.util.Iterator;
import java.util.Map;

public class FindMessageStatusesProcedure extends StoredProcedure {

    public FindMessageStatusesProcedure(DataSource dataSource) {

        super(dataSource,"FindMessageStatuses");
        compile();

    }

    protected void printMap(Map results) {
        for (Iterator it = results.entrySet().iterator(); it.hasNext(); ) {
            System.out.println(it.next());
        }
    }

}







