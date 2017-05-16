package hotel.webapp;

import hotel.model.Room;
import hotel.model.exceptions.ApplicationException;
import hotel.model.exceptions.DuplicateRoomNumberException;
import hotel.model.exceptions.RoomHasReservationException;
import hotel.model.exceptions.RoomNotFoundException;
import hotel.webapp.forms.RoomForm;
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
public class RoomsServlet extends BaseServlet
{

    private static final String LIST_JSP = "/templates/rooms/list.jsp";
    public static final String URL_MAPPING = "/rooms/";

    private final static Logger log = LoggerFactory.getLogger(RoomsServlet.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        log.debug("GET ...");

        System.out.println(request.getPathInfo());

        if (!request.getPathInfo().equals("/")) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        showRoomsList(request, response);
    }


    private void actionAdd(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        Room room = RoomForm.process(request);

        if (room == null) {
            log.debug("Form data invalid");
            request.setAttribute("error", "You entered invalid data");
            showRoomsList(request, response);
            return;
        }

        try {
            getRoomManager().create(room);
            response.sendRedirect(request.getContextPath() + URL_MAPPING);
        } catch (DuplicateRoomNumberException e) {
            request.setAttribute("error", String.format("Room with number %d already exists", room.getNumber()));
            showRoomsList(request, response);
        } catch (ApplicationException e) {
            log.error("Cannot add room", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    private void actionDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        Long id = Long.valueOf(request.getParameter("id"));

        if (id == null) {
            response.sendError(404, "No room specified");
            return;
        }

        try {
            Room room = getRoomManager().find(id);

            if (room == null) {
                response.sendError(404, "Room not found");
                return;
            }

            getRoomManager().delete(room);
            log.debug("redirecting after POST");
            response.sendRedirect(request.getContextPath() + URL_MAPPING);
        } catch(RoomNotFoundException | RoomHasReservationException e) {
            response.sendRedirect(request.getContextPath() + URL_MAPPING);
        }
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        //support non-ASCII characters in form
        request.setCharacterEncoding("utf-8");
        //action specified by pathInfo
        String action = request.getPathInfo();
        log.debug("POST ... {}", action);
        switch (action) {
            case "/add":
                actionAdd(request, response);
                return;
            case "/delete":
                actionDelete(request, response);
                return;
            case "/update":
                //TODO
                return;
            default:
                log.error("Unknown action " + action);
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Unknown action " + action);
        }
    }

    /**
     * Stores the list of Rooms to request attribute "Rooms" and forwards to the JSP to display it.
     */
    private void showRoomsList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        log.debug("showing table of Rooms");

        List<Room> rooms = getRoomManager().findAll();
        Map<Long, Integer> reservations = rooms.stream()
                .collect(Collectors.toMap(Room::getId, r -> getHotelManager().findReservationByRoom(r).size()));

        request.setAttribute("rooms", rooms);
        request.setAttribute("reservations", reservations);
        request.getRequestDispatcher(LIST_JSP).forward(request, response);
    }

}