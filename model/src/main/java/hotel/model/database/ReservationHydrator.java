package hotel.model.database;

import hotel.model.exceptions.ApplicationException;
import hotel.model.Customer;
import hotel.model.Reservation;
import hotel.model.Room;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Logger;

public class ReservationHydrator
{

    private Hydrator<Reservation> hydrator;

    public ReservationHydrator()
    {
        this.hydrator = new Hydrator<>(Reservation.class);
    }

    public Reservation hydrate(ResultSet row, Room room, Customer customer) throws ApplicationException
    {
        try {
            if(row.getLong("room_id") != room.getId()) {
                throw new IllegalArgumentException("Room doesn't match one in reservation");
            }
            if(row.getLong("customer_id") != customer.getId()) {
                throw new IllegalArgumentException("Customer doesn't match one in reservation");
            }
        } catch(SQLException e) {
            throw new ApplicationException("Error when checking room and customer", e);
        }

        Reservation reservation = hydrator.hydrate(row);

        try {
            Field roomField = reservation.getClass().getDeclaredField("room");
            roomField.setAccessible(true);
            roomField.set(reservation, room);

            Field customerField = reservation.getClass().getDeclaredField("customer");
            customerField.setAccessible(true);
            customerField.set(reservation, customer);
        } catch(NoSuchFieldException | IllegalAccessException e) {
            throw new IllegalStateException();
        }

        return reservation;
    }

}
