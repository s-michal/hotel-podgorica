package hotel.webapp.forms;

import hotel.model.Room;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;

public class RoomForm
{

    public static Room process(HttpServletRequest request)
    {
        long number;
        int capacity;
        int floor;
        BigDecimal pricePerDay;

        //getting POST parameters from form
        try {
            number = Long.parseLong(request.getParameter("number"));
            capacity = Integer.parseInt(request.getParameter("capacity"));
            floor = Integer.parseInt(request.getParameter("floor"));
            pricePerDay = BigDecimal.valueOf(Double.parseDouble(request.getParameter("pricePerDay")));
        } catch(NumberFormatException e) {
            return null;
        }

        try {
            return new Room(number, capacity, floor, pricePerDay);
        } catch (IllegalArgumentException e) {
            return null;
        }

    }

}
