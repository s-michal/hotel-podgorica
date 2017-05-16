package hotel.model.database;

import javax.sql.DataSource;

import org.apache.derby.jdbc.EmbeddedDataSource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import java.util.List;
import java.util.Objects;

public class DatasourceFactory
{

    private static final String MAIN_SQL_FILE = "/sql/up.sql";

    public static DataSource create(String name)
    {
        return create(name, new String[] {"/sql/up.sql"});
    }

    public static DataSource create(String name, String migrations[])
    {
        Objects.nonNull(name);
        Objects.nonNull(migrations);

        EmbeddedDataSource ds = new EmbeddedDataSource();

        // we will use in memory database
        ds.setDatabaseName(name);
        // database is created automatically if it does not exist yet
        ds.setCreateDatabase("create");

        for (String script : migrations) {
            new ResourceDatabasePopulator(new ClassPathResource(script)).execute(ds);
        }

        return ds;
    }

}
