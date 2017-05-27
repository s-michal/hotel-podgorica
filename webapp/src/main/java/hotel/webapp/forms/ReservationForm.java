package hotel.webapp.forms;

import hotel.model.*;
import hotel.model.exceptions.*;
import hotel.webapp.forms.exceptions.ValidationException;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Objects;

public class ReservationForm
{
    private HotelManager reservations;
    private CustomerManager customers;
    private RoomManager rooms;

    public ReservationForm(HotelManager manager, CustomerManager customers, RoomManager rooms)
    {
        Objects.requireNonNull(manager);
        Objects.requireNonNull(customers);
        Objects.requireNonNull(rooms);


        this.reservations = manager;
        this.customers = customers;
        this.rooms = rooms;
    }

    public void process(HttpServletRequest request) throws ValidationException, ApplicationException
    {
        Long roomId = Long.parseLong(request.getParameter("roomId"));
        Long customerId = Long.parseLong(request.getParameter("customerId"));

        request.setAttribute("roomId", roomId);
        request.setAttribute("customerId", customerId);

        LocalDate since, until;
        try {
            since = LocalDate.parse(request.getParameter("since"));
            until = LocalDate.parse(request.getParameter("until"));
        } catch (DateTimeParseException e) {
            throw new ValidationException(e);
        }

        request.setAttribute("since", since);
        request.setAttribute("until", until);

        try {
            create(roomId, customerId, since, until);
        } catch (IllegalArgumentException e) {
            throw new ValidationException(e);
        }
    }

    private void create(Long roomId, Long customerId, LocalDate since, LocalDate until) throws ApplicationException, ValidationException
    {
        Customer customer;
        Room room;

        try {
            customer = customers.find(customerId);
            room = rooms.find(roomId);
            reservations.placeReservation(new Reservation(room, customer, since, until));
        } catch (CustomerNotFoundException | RoomNotFoundException | RoomHasReservationException e) {
            throw new ValidationException(e.getMessage());
        }
    }

}
