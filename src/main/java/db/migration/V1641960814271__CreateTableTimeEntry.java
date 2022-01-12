package db.migration;

import static org.jooq.impl.DSL.*;
import static org.jooq.impl.SQLDataType.*;

import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;

/**
* @see https://www.jooq.org/doc/3.1/manual/sql-building/ddl-statements/
*/
public class V1641960814271__CreateTableTimeEntry extends BaseJavaMigration {

    @Override
    public void migrate(Context context) throws Exception {
        var create = using(context.getConnection());
         create.transaction(configuration -> {
             using(configuration)
                 .createTableIfNotExists("time_entry")
                     .column("id", BIGINT.identity(true))
                     .column("description", VARCHAR(100).nullable(true))
                     .column("start", TIMESTAMPWITHTIMEZONE.nullable(false))
                     .column("stop", TIMESTAMPWITHTIMEZONE.nullable(true))
                     .column("user_id", BIGINT.nullable(true))
                     .column("project_id", BIGINT.nullable(true))
                 .constraints(
                     primaryKey("id"),
                     constraint("time_entry_user_fk").foreignKey("user_id").references("user", "id"),
                     constraint("time_entry_project_fk").foreignKey("project_id").references("project", "id"))
                 .execute();
         });
    }
}