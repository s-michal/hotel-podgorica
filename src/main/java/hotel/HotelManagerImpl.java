package hotel;

import com.sun.org.apache.regexp.internal.RE;
import hotel.database.Hydrator;
import hotel.database.Persister;
import hotel.database.ReservationHydrator;
import hotel.exceptions.ReservationNotFoundException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class HotelManagerImpl implements HotelManager
{

    private static final String QUERY = "SELECT * FROM \"reservation\"";


    private DataSource dataSource;
    private ReservationHydrator reservationHydrator;
    private Hydrator<Customer> customerHydrator;
    private Hydrator<Room> roomHydrator;
    private Logger logger;
    private Persister<Reservation> persister;

    public HotelManagerImpl(
            DataSource dataSource,
            ReservationHydrator reservationHydrator,
            Hydrator<Customer> customerHydrator,
            Hydrator<Room> roomHydrator,
            Logger logger,
            Persister<Reservation> persister
    )
    {
        this.dataSource = dataSource;
        this.reservationHydrator = reservationHydrator;
        this.customerHydrator = customerHydrator;
        this.roomHydrator = roomHydrator;
        this.logger = logger;
        this.persister = persister;
    }

    public Reservation find(long id) throws ReservationNotFoundException, ApplicationException
    {
        try (Connection conn = dataSource.getConnection()) {

            PreparedStatement statement = conn.prepareStatement(
                    QUERY + "WHERE \"id\" = ?",
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_UPDATABLE
            );

            statement.setLong(1, id);

            List<Reservation> reservations = executeQueryForMultipleRows(statement.executeQuery());

            if(reservations.isEmpty()) {
                throw new ReservationNotFoundException();
            }

            return reservations.get(0);
        } catch (SQLException e) {
            String message = "Find was unsuccessful";
            log(e, message);
            throw new ApplicationException(message, e);
        }
    }

    public List<Collection> findCustomerReservations(Customer customer)
    {
        return null;
    }

    public void placeReservation(Reservation reservation) throws ApplicationException
    {
        Objects.requireNonNull(reservation);
        long id = persister.insert(reservation);
        reservation.setId(id);
    }

    public void delete(Reservation reservation) throws ReservationNotFoundException
    {
        Objects.requireNonNull(reservation);
        if(reservation.getId() == null) {
            throw new ReservationNotFoundException();
        }

        try (Connection conn = dataSource.getConnection()) {
            PreparedStatement statement = conn.prepareStatement(
                    "DELETE FROM \"reservation\" WHERE \"id\" = ?");

            statement.setLong(1, reservation.getId());

            statement.execute();
        } catch (SQLException e) {
            log(e, "Reservation couldn't be deleted");
        }
    }

    public List<Reservation> findReservationByRoom(Room room) throws ApplicationException
    {
        Objects.requireNonNull(room);
        Objects.requireNonNull(room.getId());

        try (Connection conn = dataSource.getConnection()) {
            PreparedStatement statement = conn.prepareStatement(
                    QUERY+ "WHERE \"room_id\" = ?",
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_UPDATABLE
            );
            statement.setLong(1, room.getId());
            return executeQueryForMultipleRows(statement.executeQuery());
        } catch (SQLException e) {
            String message = "Find by room was unsuccessful";
            log(e, message);
            throw new ApplicationException(message, e);
        }
    }

    public List<Room> findAvailableRooms(Date since, Date until) throws ApplicationException
    {
        return new ArrayList<>();
    }

    public List<Reservation> findAll() throws ApplicationException
    {
        try (Connection conn = dataSource.getConnection()) {
            PreparedStatement statement = conn.prepareStatement(
                    QUERY,
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_UPDATABLE
            );
            return executeQueryForMultipleRows(statement.executeQuery());
        } catch (SQLException e) {
            String message = "Find all was unsuccessful";
            log(e, message);
            throw new ApplicationException(message, e);
        }
    }

    private List<Reservation> executeQueryForMultipleRows(ResultSet result) throws ApplicationException, SQLException
    {
        Set<Long> roomIds = new HashSet<>();
        Set<Long> customerIds = new HashSet<>();

        while(result.next()) {
                roomIds.add(result.getLong("room_id"));
                customerIds.add(result.getLong("customer_id"));
        }

        List<Reservation> list = new ArrayList<>();

        if(roomIds.isEmpty()) {
            return list;
        }

        result.beforeFirst();

        Map<Long, Object> rooms = findAssociations("room", roomIds, roomHydrator);
        Map<Long, Object> customers = findAssociations("customer", customerIds, customerHydrator);

        while(result.next()) {
            list.add(
                    reservationHydrator.hydrate(
                            result,
                            (Room) rooms.get(result.getLong("room_id")),
                            (Customer) customers.get(result.getLong("customer_id"))
                    )
            );
        }

        return list;
    }

    private Map<Long, Object> findAssociations(String table, Set<Long> ids, Hydrator hydrator) throws ApplicationException
    {
        String list = String.join(", ", Collections.nCopies(ids.size(), "?"));
        try (Connection conn = dataSource.getConnection()) {
            PreparedStatement statement = conn.prepareStatement(
                    String.format("SELECT * FROM \"%s\" WHERE \"id\" IN (%s)", table, list)
            );

            int i = 1;
            for (long id : ids) {
                statement.setLong(i++, id);
            }
            ResultSet result = statement.executeQuery();

            Map<Long, Object> assocation = new HashMap<>();
            while(result.next()) {
                assocation.put(result.getLong("id"), hydrator.hydrate(result));
            }

            return assocation;

        } catch (SQLException e) {
            //log(e, "Find all was unsuccessful");
            throw new ApplicationException(String.format("Couln't load data from %s", table), e);
        }
    }

    private void log(Throwable e, String message)
    {
        if(logger == null) {
            return;
        }
        logger.log(Level.SEVERE, message, e);
    }

}
