package db.migration;

import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;

import static org.jooq.impl.DSL.using;
import static org.jooq.impl.SQLDataType.VARCHAR;

/**
* @see https://www.jooq.org/doc/3.1/manual/sql-building/ddl-statements/
*/
public class V1642859843601__AddColumnsTimestampAndDateFormatsOnUser extends BaseJavaMigration {

    @Override
    public void migrate(Context context) throws Exception {
        var create = using(context.getConnection());
         create.transaction(configuration -> {
             using(configuration)
                 .alterTable("user")
                     .addColumn("timezone", VARCHAR(60)
                            .nullable(true))
                        .execute();

             using(configuration)
                 .alterTable("user")
                    .addColumn("date_format", VARCHAR(60)
                            .nullable(true))
                        .execute();

             using(configuration)
                 .alterTable("user")
                    .addColumn("time_format", VARCHAR(60)
                            .nullable(true))
                        .execute();
         });
    }
}