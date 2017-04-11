package hotel.webapp.forms;

import hotel.model.Customer;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class CustomerForm
{

    public static Customer process(HttpServletRequest request)
    {
        String name = request.getParameter("name");
        String address = request.getParameter("address");


        LocalDate birthDate;
        try {
            birthDate = LocalDate.parse(request.getParameter("birthDate"));
        } catch(DateTimeParseException e) {
            return null;
        }

        try {
            return new Customer(name, address, birthDate);
        } catch (IllegalArgumentException e) {
            return null;
        }

    }

}
