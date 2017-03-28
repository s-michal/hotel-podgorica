package hotel.database;

public class Utils
{

    public static String propertyNameToColumnName(String name)
    {
        String regex = "([a-z])([A-Z]+)";
        String replacement = "$1_$2";
        return name.replaceAll(regex, replacement).toLowerCase();
    }

}
