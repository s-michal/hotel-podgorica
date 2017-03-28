package hotel.database;

import hotel.ApplicationException;
import sun.reflect.ReflectionFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class Hydrator<T>
{

    private Class<T> aClass;

    private Logger logger;

    public Hydrator(Class<T> aClass, Logger logger)
    {
        this.aClass = aClass;
        this.logger = logger;
    }

    public T hydrate(ResultSet row) throws ApplicationException
    {
        try {
            T instance = prepareInstance();

            for (Field field: aClass.getDeclaredFields()) {
                setProperty(instance, field, row);
            }

            return instance;

        } catch(InstantiationException | IllegalAccessException |InvocationTargetException | SQLException e) {
            String message = "Hydration failed";

            if(logger != null) {
                logger.log(Level.SEVERE, message, e);
            }
            throw new ApplicationException(message, e);
        }
    }

    private T prepareInstance() throws InvocationTargetException, InstantiationException, IllegalAccessException
    {
        ReflectionFactory rf = ReflectionFactory.getReflectionFactory();
        Constructor intConstr = rf.newConstructorForSerialization(aClass);
        return aClass.cast(intConstr.newInstance());
    }

    private void setProperty(T instance, Field field, ResultSet result) throws SQLException, IllegalAccessException
    {
        field.setAccessible(true);

        Object value = result.getObject(Utils.propertyNameToColumnName(field.getName()));

        if(value instanceof Date) {
            value = toLocalDate((Date)value);
        }

        field.set(instance, value);
    }
    private LocalDate toLocalDate(Date date)
    {
        return date == null ? null : date.toLocalDate();
    }

}
