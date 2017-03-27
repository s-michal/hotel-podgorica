package hotel;

import org.apache.derby.jdbc.EmbeddedDataSource;

import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;

abstract class TestWithDatabase
{

    private EmbeddedDataSource ds;

    protected void setUpDatabase(String name) throws Exception
    {
        ds = new EmbeddedDataSource();
        // we will use in memory database
        ds.setDatabaseName("memory:" + name);
        // database is created automatically if it does not exist yet
        ds.setCreateDatabase("create");
        executeSqlScript(CustomerManagerImpl.class.getResource("/sql/schema.sql"));
    }


    private void executeSqlScript(URL scriptUrl) throws SQLException
    {
        Connection conn = null;
        try {
            conn = ds.getConnection();
            for (String sqlStatement : readSqlStatements(scriptUrl)) {
                if (!sqlStatement.trim().isEmpty()) {
                    conn.prepareStatement(sqlStatement).executeUpdate();
                }
            }
        } finally {
            if (conn != null) {
                conn.close();
            }
        }
    }

    private String[] readSqlStatements(URL url)
    {
        try {
            char buffer[] = new char[256];
            StringBuilder result = new StringBuilder();
            InputStreamReader reader = new InputStreamReader(url.openStream(), "UTF-8");
            while (true) {
                int count = reader.read(buffer);
                if (count < 0) {
                    break;
                }
                result.append(buffer, 0, count);
            }
            return result.toString().split(";");
        } catch (IOException ex) {
            throw new RuntimeException("Cannot read " + url, ex);
        }
    }

}
