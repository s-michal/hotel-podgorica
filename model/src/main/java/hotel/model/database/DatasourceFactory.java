package hotel.model.database;

import javax.sql.DataSource;

import org.apache.derby.jdbc.EmbeddedDataSource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import java.util.Objects;

public class DatasourceFactory
{

    public static DataSource create(String name)
    {
        Objects.nonNull(name);
        EmbeddedDataSource ds = new EmbeddedDataSource();
        // we will use in memory database
        ds.setDatabaseName(name);
        // database is created automatically if it does not exist yet
        ds.setCreateDatabase("create");

        new ResourceDatabasePopulator(new ClassPathResource("sql/up.sql")).execute(ds);

        return ds;
    }

}
