package db.migration;

import static org.jooq.impl.DSL.*;
import static org.jooq.impl.SQLDataType.*;

import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;

/**
* @see <a href="https://www.jooq.org/doc/3.1/manual/sql-building/ddl-statements/">docs</a>
*/
public class V1643682226718__AddColumnColorOnProject extends BaseJavaMigration {

    @Override
    public void migrate(Context context) throws Exception {
        var create = using(context.getConnection());
         create.transaction(configuration -> {
             using(configuration)
                 .alterTableIfExists("project")
                     .addColumn("color", VARCHAR(7))
                 .execute();
         });
    }
}