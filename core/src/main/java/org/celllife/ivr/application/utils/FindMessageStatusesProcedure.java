package org.celllife.ivr.application.utils;

import org.springframework.jdbc.core.SqlParameter;
import org.springframework.jdbc.object.StoredProcedure;

import javax.sql.DataSource;
import java.sql.Types;
import java.util.Iterator;
import java.util.Map;

/**
 * This class is to execute the stored procedure FindMessageStatuses. It has one parameter, the campaign id "camp".
 */
public class FindMessageStatusesProcedure extends StoredProcedure {

    /**
     * This constructor needs to be used in a spring xml file. In this case, see spring-jdbc.xml.
     * @param dataSource The datasource that contains the stored procedure.
     */
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