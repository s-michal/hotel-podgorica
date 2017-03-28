package hotel.database;

import hotel.ApplicationException;

import javax.sql.DataSource;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class Persister<T>
{

    private String table;

    private DataSource dataSource;

    private Logger logger;

    public Persister(String table, DataSource dataSource, Logger logger)
    {
        Objects.requireNonNull(table);
        Objects.requireNonNull(dataSource);

        this.table = table;
        this.dataSource = dataSource;
        this.logger = logger;
    }

    public void update(T entity, long id) throws ApplicationException
    {
        try (Connection conn = dataSource.getConnection()) {
            StringJoiner sets = new StringJoiner(", ");

            List<Field> fields = getFields(entity);

            for (Field field : fields) {

                String columnName = Utils.propertyNameToColumnName(field.getName());

                if(field.isAnnotationPresent(Association.class)) {
                    columnName += "_id";
                }

                sets.add(String.format("\"%s\" = ?", columnName));
            }

            String query = String.format("UPDATE \"%s\" SET %s WHERE \"id\" = ?", table, sets);

            PreparedStatement statement = conn.prepareStatement(query);

            int i = 1;

            for (Field field : fields) {
                field.setAccessible(true);

                Object value;
                if(field.isAnnotationPresent(Association.class)) {
                    Object association = field.get(entity);
                    Field idField = association.getClass().getDeclaredField("id");
                    idField.setAccessible(true);
                    value = idField.get(association);
                } else {
                    value = field.get(entity);
                }

                set(statement, i++, field.get(entity));
            }

            statement.setLong(i, id);

            statement.execute();
        } catch (SQLException | IllegalAccessException | NoSuchFieldException e) {
            String message = "Update failed";
            log(e, message);
            throw new ApplicationException(message, e);
        }


    }

    public long insert(T entity) throws ApplicationException
    {
        try (Connection conn = dataSource.getConnection()) {
            StringJoiner columns = new StringJoiner(", ");
            StringJoiner values = new StringJoiner(", ");
            List<Field> fields = getFields(entity);

            for (Field field : fields) {

                String columnName = Utils.propertyNameToColumnName(field.getName());

                if(field.isAnnotationPresent(Association.class)) {
                    columnName += "_id";
                }
                columns.add(String.format("\"%s\"", columnName));
                values.add("?");
            }


            String query = String.format("INSERT INTO \"%s\" (%s) VALUES (%s)", table, columns, values);

            PreparedStatement statement = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

            int i = 1;

            for (Field field : fields) {
                field.setAccessible(true);

                Object value;
                if(field.isAnnotationPresent(Association.class)) {
                    Object association = field.get(entity);
                    Field idField = association.getClass().getDeclaredField("id");
                    idField.setAccessible(true);
                    value = idField.get(association);
                } else {
                    value = field.get(entity);
                }

                set(statement, i++, value);
            }

            statement.execute();

            return getId(statement.getGeneratedKeys());

        } catch (SQLException | IllegalAccessException | NoSuchFieldException e) {
            String message = "Insert failed";
            log(e, message);
            throw new ApplicationException(message, e);
        }
    }

    private void set(PreparedStatement statement, int position, Object value) throws SQLException
    {
        if (value instanceof Long) {
            statement.setLong(position, (Long) value);
        } else if (value instanceof String) {
            statement.setString(position, (String) value);
        } else if (value instanceof LocalDate) {
            statement.setDate(position, Utils.toSqlDate((LocalDate) value));
        } else if (value instanceof BigDecimal) {
            statement.setBigDecimal(position, (BigDecimal) value);
        } else if (value instanceof Integer) {
            statement.setInt(position, (Integer) value);
        }
    }

    private List<Field> getFields(T entity)
    {
        return Arrays.stream(entity.getClass().getDeclaredFields())
                .filter(f -> !f.getName().equals("id"))
                .collect(Collectors.toList());
    }

    private long getId(ResultSet key) throws SQLException
    {
        if (key.getMetaData().getColumnCount() != 1) {
            throw new IllegalArgumentException("Given ResultSet contains more columns");
        }
        if (key.next()) {
            Long result = key.getLong(1);
            if (key.next()) {
                throw new IllegalArgumentException("Given ResultSet contains more rows");
            }
            return result;
        } else {
            throw new IllegalArgumentException("Given ResultSet contain no rows");
        }
    }

    private void log(Throwable e, String message)
    {
        if (logger == null) {
            return;
        }
        logger.log(Level.SEVERE, message, e);
    }


}