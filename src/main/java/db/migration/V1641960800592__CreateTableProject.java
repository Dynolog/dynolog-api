package db.migration;

import static org.jooq.impl.DSL.*;
import static org.jooq.impl.SQLDataType.*;

import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;

/**
* @see https://www.jooq.org/doc/3.1/manual/sql-building/ddl-statements/
*/
public class V1641960800592__CreateTableProject extends BaseJavaMigration {

    @Override
    public void migrate(Context context) throws Exception {
        var create = using(context.getConnection());
         create.transaction(configuration -> {
             using(configuration)
                 .createTableIfNotExists("project")
                     .column("id", BIGINT.identity(true))
                     .column("name", VARCHAR(100).nullable(false))
                     .column("user_id", BIGINT.nullable(true))
                 .constraints(
                     primaryKey("id"),
                     constraint("project_user_fk").foreignKey("user_id").references("user", "id"))
                 .execute();
         });
    }
}