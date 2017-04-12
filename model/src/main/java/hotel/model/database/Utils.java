package hotel.model.database;

import java.sql.Date;
import java.time.LocalDate;

public class Utils
{

    public static String propertyNameToColumnName(String name)
    {
        String regex = "([a-z])([A-Z]+)";
        String replacement = "$1_$2";
        return name.replaceAll(regex, replacement).toLowerCase();
    }

    public static Date toSqlDate(LocalDate localDate)
    {
        return localDate == null ? null : Date.valueOf(localDate);
    }


}
