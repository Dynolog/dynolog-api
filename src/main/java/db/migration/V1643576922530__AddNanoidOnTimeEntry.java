package db.migration;

import static org.jooq.impl.DSL.*;
import static org.jooq.impl.SQLDataType.*;

import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;

/**
* @see https://www.jooq.org/doc/3.1/manual/sql-building/ddl-statements/
*/
public class V1643576922530__AddNanoidOnTimeEntry extends BaseJavaMigration {

    @Override
    public void migrate(Context context) throws Exception {
        var create = using(context.getConnection());
        create.transaction(configuration -> {
            using(configuration)
                    .alterTableIfExists("time_entry")
                    .addColumn("nanoid", BINARY.nullable(true))
                    .execute();

            using(configuration)
                    .alterTable("time_entry")
                    .add(constraint("time_entry_unique_nanoid").unique("nanoid"))
                    .execute();
        });
    }
}