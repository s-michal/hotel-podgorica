package hotel.model.database;

import hotel.model.exceptions.ApplicationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sun.reflect.ReflectionFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.time.LocalDate;

public class Hydrator<T>
{

    private Class<T> aClass;

    private static Logger logger = LoggerFactory.getLogger(Hydrator.class);

    public Hydrator(Class<T> aClass)
    {
        this.aClass = aClass;
    }

    public T hydrate(ResultSet row) throws ApplicationException
    {
        try {
            T instance = prepareInstance();

            for (Field field: aClass.getDeclaredFields()) {
                setProperty(instance, field, row);
            }

            return instance;

        } catch(InstantiationException | IllegalAccessException |InvocationTargetException | NoSuchMethodException | SQLException e) {
            String message = "Hydration failed";

            if(logger != null) {
                logger.error(message, e);
            }
            throw new ApplicationException(message, e);
        }
    }

    private T prepareInstance() throws InvocationTargetException, InstantiationException, IllegalAccessException, NoSuchMethodException
    {
        ReflectionFactory rf = ReflectionFactory.getReflectionFactory();
        // newConstructorForSerialization worked without Object.class.getDeclaredConstructor()
        Constructor intConstr = rf.newConstructorForSerialization(aClass, Object.class.getDeclaredConstructor());
        return aClass.cast(intConstr.newInstance());
    }

    private void setProperty(T instance, Field field, ResultSet result) throws SQLException, IllegalAccessException
    {
        field.setAccessible(true);

        String columnName = Utils.propertyNameToColumnName(field.getName());

        if(!containsColumn(columnName, result)) {
            return;
        }

        Object value = result.getObject(columnName);

        if(value instanceof Date) {
            value = toLocalDate((Date)value);
        }

        field.set(instance, value);
    }

    private boolean containsColumn(String name, ResultSet result) throws SQLException
    {
        ResultSetMetaData rsmd = result.getMetaData();
        int columns = rsmd.getColumnCount();
        for (int x = 1; x <= columns; x++) {
            if (name.equals(rsmd.getColumnName(x))) {
                return true;
            }
        }
        return false;
    }

    private LocalDate toLocalDate(Date date)
    {
        return date == null ? null : date.toLocalDate();
    }

}
