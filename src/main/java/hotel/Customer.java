package hotel;

import java.util.Date;

/**
 * Created by xsustera on 8.3.17.
 */
public class Customer {

    static Long id;
    static String name;
    static String Address;
    static Date BirthDate;

    public static Long getId() {
        return id;
    }

    public static void setId(Long id) {
        Customer.id = id;
    }

    public static String getName() {
        return name;
    }

    public static void setName(String name) {
        Customer.name = name;
    }

    public static String getAddress() {
        return Address;
    }

    public static void setAddress(String address) {
        Address = address;
    }

    public static Date getBirthDate() {
        return BirthDate;
    }

    public static void setBirthDate(Date birthDate) {
        BirthDate = birthDate;
    }
}
