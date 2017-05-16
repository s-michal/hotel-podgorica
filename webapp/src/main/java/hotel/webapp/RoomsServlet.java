package hotel.webapp;

import hotel.model.Room;
import hotel.model.exceptions.ApplicationException;
import hotel.model.exceptions.RoomHasReservationException;
import hotel.model.exceptions.RoomNotFoundException;
import hotel.webapp.forms.RoomForm;
import hotel.webapp.forms.exceptions.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@WebServlet(RoomsServlet.URL_MAPPING + "*")
public class RoomsServlet extends AbstractServlet
{

    private static final String LIST_JSP = "/templates/rooms/list.jsp";
    static final String URL_MAPPING = "/rooms/";

    private final static Logger log = LoggerFactory.getLogger(RoomsServlet.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        request.setAttribute("formTarget", "add");

        log.debug("GET ...");

        switch (request.getPathInfo()) {
            case "/update":
                getUpdate(request, response);
                return;
            case "/":
                showRoomList(request, response);
                return;
        }

        response.sendError(HttpServletResponse.SC_NOT_FOUND);
    }


    void actionAdd(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        try {
            createForm(request).process(request);
            response.sendRedirect(request.getContextPath() + URL_MAPPING);
        } catch (ValidationException e) {
            log.debug("Form data invalid");
            request.setAttribute("error", "You entered invalid data");
            showRoomList(request, response);
        } catch (ApplicationException e) {
            log.error("Cannot add room", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    private void getUpdate(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    {
        Long id = Long.valueOf(request.getParameter("id"));

        if (id == null) {
            response.sendError(404, "No room specified");
            return;
        }

        request.setAttribute("formTarget", "update?id=" + id);

        RoomForm form = createForm(request);

        try {
            form.setId(id);
        } catch (RoomNotFoundException e) {
            log.debug("Room not found");
            response.sendRedirect(request.getContextPath() + URL_MAPPING);
            return;
        }

        form.render();
        showRoomList(request, response);
    }

    void postUpdate(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        Long id = Long.valueOf(request.getParameter("id"));

        if (id == null) {
            response.sendError(404, "No room specified");
            return;
        }


        request.setAttribute("formTarget", "update?id=" + id);

        RoomForm form = createForm(request);

        try {
            form.setId(id);
            form.process(request);
            response.sendRedirect(request.getContextPath() + URL_MAPPING);
        } catch (RoomNotFoundException e) {
            log.debug("Room not found");
            response.sendRedirect(request.getContextPath() + URL_MAPPING);
        } catch (ValidationException e) {
            log.debug("Form data invalid");
            request.setAttribute("error", "You entered invalid data");
            showRoomList(request, response);
        } catch (ApplicationException e) {
            log.error("Cannot add room", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    void actionDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        Long id = Long.valueOf(request.getParameter("id"));

        if (id == null) {
            response.sendError(404, "No room specified");
            return;
        }

        try {
            Room room = getRoomManager().find(id);

            if (room == null) {
                response.sendError(404, "Customer not found");
                return;
            }

            getRoomManager().delete(room);
            log.debug("redirecting after POST");
            response.sendRedirect(request.getContextPath() + URL_MAPPING);
        } catch (RoomNotFoundException e) {
            log.error("Cannot remove customer", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        } catch(RoomHasReservationException e) {
            String message = "Cannot remove room with reservations";
            log.error(message);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, message);
        }
    }

    private RoomForm createForm(HttpServletRequest request)
    {
        return new RoomForm(request, getRoomManager());
    }

    /**
     * Stores the list of Customers to request attribute "Customers" and forwards to the JSP to display it.
     */
    private void showRoomList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        log.debug("showing table of Customers");

        List<Room> rooms = getRoomManager().findAll();

        Map<Long,Integer> reservations = rooms.stream()
                .collect(Collectors.toMap(Room::getId, c -> getHotelManager().findReservationByRoom(c).size()));

        request.setAttribute("rooms", rooms);
        request.setAttribute("reservations", reservations);
        request.getRequestDispatcher(LIST_JSP).forward(request, response);
        getRoomManager();
    }

}