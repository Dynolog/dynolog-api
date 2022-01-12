package db.migration;

import static org.jooq.impl.DSL.*;
import static org.jooq.impl.SQLDataType.*;

import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;

/**
* @see https://www.jooq.org/doc/3.1/manual/sql-building/ddl-statements/
*/
public class V1641399127052__CreateTableUser extends BaseJavaMigration {

    @Override
    public void migrate(Context context) throws Exception {
        var create = using(context.getConnection());
        create.transaction(configuration -> {
            using(configuration)
                .createTableIfNotExists("user")
                    .column("id", BIGINT.identity(true))
                    .column("name", VARCHAR(100).nullable(false))
                    .column("email", VARCHAR(100).nullable(true))
                    .column("password", VARCHAR(100).nullable(false))
                .constraints(
                    primaryKey("id"),
                    unique("email"))
                .execute();
        });
    }
}