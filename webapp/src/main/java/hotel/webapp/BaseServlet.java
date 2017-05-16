package hotel.webapp;

import hotel.model.CustomerManager;
import hotel.model.HotelManager;
import hotel.model.RoomManager;

import javax.servlet.http.HttpServlet;

public abstract class BaseServlet extends HttpServlet
{
    protected CustomerManager getCustomerManager()
    {
        return (CustomerManager) getServletContext().getAttribute("customerManager");
    }

    protected RoomManager getRoomManager()
    {
        return (RoomManager) getServletContext().getAttribute("roomManager");
    }

    protected HotelManager getHotelManager()
    {
        return (HotelManager) getServletContext().getAttribute("hotelManager");
    }

}
