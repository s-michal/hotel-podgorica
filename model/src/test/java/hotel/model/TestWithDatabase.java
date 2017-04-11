package hotel.model;

import hotel.model.database.DatasourceFactory;
import org.junit.After;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;

import javax.sql.DataSource;

abstract class TestWithDatabase
{

    private DataSource ds;

    protected DataSource getDataSource() throws Exception
    {
        if(ds == null) {
            ds = DatasourceFactory.create("memory:tests");
        }

        return ds;
    }

    @After
    public void tearDown() throws Exception
    {
        new ResourceDatabasePopulator(new ClassPathResource("sql/down.sql")).execute(ds);
    }


}
