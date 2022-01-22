package db.migration;

import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;

import static org.jooq.impl.DSL.using;
import static org.jooq.impl.SQLDataType.DECIMAL;

/**
* @see https://www.jooq.org/doc/3.1/manual/sql-building/ddl-statements/
*/
public class V1642861057959__AddColumnHourlyHateOnProject extends BaseJavaMigration {

    @Override
    public void migrate(Context context) throws Exception {
        var create = using(context.getConnection());
         create.transaction(configuration -> {
             using(configuration)
                 .alterTable("project")
                     .addColumn("hourly_hate", DECIMAL(10, 2)
                             .nullable(false))
                .execute();
         });
    }
}