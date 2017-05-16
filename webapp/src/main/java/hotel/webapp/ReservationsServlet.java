package hotel.webapp;

import hotel.model.*;
import hotel.model.exceptions.ApplicationException;
import hotel.webapp.forms.ReservationForm;
import hotel.webapp.forms.exceptions.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@WebServlet(ReservationsServlet.URL_MAPPING + "*")
public class ReservationsServlet extends HttpServlet
{

    private static final String LIST_JSP = "/templates/reservations/list.jsp";
    public static final String URL_MAPPING = "/reservations/";

    private final static Logger log = LoggerFactory.getLogger(ReservationsServlet.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        request.setAttribute("formTarget", "add");

        log.debug("GET ...");

        showReservations(request, response);
    }


    private void actionAdd(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        try {
            createForm(request).process(request);
            response.sendRedirect(request.getContextPath() + URL_MAPPING);
        } catch (ValidationException e) {
            log.debug("Form data invalid");
            request.setAttribute("error", "You entered invalid data");
            showReservations(request, response);
        } catch (ApplicationException e) {
            log.error("Cannot place reservation", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

//    private void actionDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
//    {
//        Long id = Long.valueOf(request.getParameter("id"));
//
//        if (id == null) {
//            response.sendError(404, "No customer specified");
//            return;
//        }
//
//        try {
//            Customer customer = getHotelManager().find(id);
//
//            if (customer == null) {
//                response.sendError(404, "Customer not found");
//                return;
//            }
//
//            getCustomerManager().delete(customer);
//            log.debug("redirecting after POST");
//            response.sendRedirect(request.getContextPath() + URL_MAPPING);
//        } catch (ApplicationException e) {
//            log.error("Cannot remove customer", e);
//            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
//        }
//    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        request.setAttribute("formTarget", "add");
        //support non-ASCII characters in form
        request.setCharacterEncoding("utf-8");
        //action specified by pathInfo
        String action = request.getPathInfo();
        log.debug("POST ... {}", action);
        switch (action) {
            case "/add":
                actionAdd(request, response);
                return;
            default:
                log.error("Unknown action " + action);
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Unknown action " + action);
        }
    }

    private ReservationForm createForm(HttpServletRequest request)
    {
        return new ReservationForm(request, getHotelManager(), getCustomerManager(), getRoomManager());
    }

    private CustomerManager getCustomerManager()
    {
        return (CustomerManager) getServletContext().getAttribute("customerManager");
    }

    private HotelManager getHotelManager()
    {
        return (HotelManager) getServletContext().getAttribute("hotelManager");
    }

    private RoomManager getRoomManager()
    {
        return (RoomManager) getServletContext().getAttribute("roomManager");
    }


    /**
     * Stores the list of Reservations to request attribute "Reservations" and forwards to the JSP to display it.
     */
    private void showReservations(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        try {
            log.debug("showing table of Reservations");
            request.setAttribute("reservations", getHotelManager().findAll());
            request.setAttribute("customers", getCustomers());
            request.setAttribute("rooms", getRooms());
            request.getRequestDispatcher(LIST_JSP).forward(request, response);
        } catch (ApplicationException e) {
            log.error("There was an error", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    private List<Room> getRooms()
    {
        return getRoomManager().findAll()
                .stream()
                .sorted(Comparator.comparingLong(Room::getNumber))
                .collect(Collectors.toList());
    }

    private List<Customer> getCustomers()
    {
        return getCustomerManager().findAll()
                .stream()
                .sorted(Comparator.comparing(Customer::getName))
                .collect(Collectors.toList());
    }

}
