package hotel.webapp.forms;

import hotel.model.Room;
import hotel.model.RoomManager;
import hotel.model.exceptions.ApplicationException;
import hotel.model.exceptions.DuplicateRoomNumberException;
import hotel.model.exceptions.RoomNotFoundException;
import hotel.webapp.forms.exceptions.ValidationException;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;

public class RoomForm
{
    private HttpServletRequest request;
    private RoomManager manager;

    private Room room;

    public RoomForm(HttpServletRequest request, RoomManager manager)
    {
        this.request = request;
        this.manager = manager;
    }

    public void setId(long id) throws RoomNotFoundException
    {
        room = manager.find(id);
    }

    public void render()
    {
        if (this.room == null) {
            return;
        }

        request.setAttribute("number", room.getNumber());
        request.setAttribute("capacity", room.getCapacity());
        request.setAttribute("floor", room.getFloor());
        request.setAttribute("pricePerDay", room.getPricePerDay());
    }

    public void process(HttpServletRequest request) throws ValidationException, ApplicationException
    {
        long number;
        int capacity;
        int floor;
        BigDecimal pricePerDay;

        try {
            number = Long.parseLong(request.getParameter("number"));
            capacity = Integer.parseInt(request.getParameter("capacity"));
            floor = Integer.parseInt(request.getParameter("floor"));
            pricePerDay = BigDecimal.valueOf(Double.parseDouble(request.getParameter("pricePerDay")));
        } catch (NumberFormatException e) {
            throw new ValidationException(e);
        }

        request.setAttribute("number", number);
        request.setAttribute("capacity", capacity);
        request.setAttribute("floor", floor);
        request.setAttribute("pricePerDay", pricePerDay);

        try {
            if (this.room == null) {
                create(number, capacity, floor, pricePerDay);
                return;
            }

            update(number, capacity, floor, pricePerDay);
        } catch (DuplicateRoomNumberException e) {
            throw new ValidationException(e);
        }
    }

    private void create(long number, int capacity, int floor, BigDecimal pricePerDay) throws ApplicationException, DuplicateRoomNumberException
    {
        manager.create(new Room(number, capacity, floor, pricePerDay));
    }

    private void update(long number, int capacity, int floor, BigDecimal pricePerDay) throws ApplicationException
    {
        room.update(number, capacity, floor, pricePerDay);
        manager.update(room);
    }

}
