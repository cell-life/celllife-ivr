package org.celllife.ivr.application.utils;

import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.StoredProcedure;

import javax.sql.DataSource;
import java.sql.Types;
import java.util.Iterator;
import java.util.Map;

public class FindMessageStatusesProcedure extends StoredProcedure {

    public FindMessageStatusesProcedure(DataSource dataSource) {

        super(dataSource,"FindMessageStatuses");
        SqlParameter parameter = new SqlParameter("camp",Types.BIGINT);
        SqlParameter[] parameters = {parameter};
        this.setParameters(parameters);
        compile();

    }

    protected void printMap(Map results) {
        for (Iterator it = results.entrySet().iterator(); it.hasNext(); ) {
            System.out.println(it.next());
        }
    }

}







