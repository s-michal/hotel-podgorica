package hotel.model;

import hotel.model.database.Hydrator;
import hotel.model.database.Persister;
import hotel.model.database.ReservationHydrator;
import hotel.model.exceptions.ApplicationException;
import hotel.model.exceptions.ReservationNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class HotelManagerImpl implements HotelManager
{

    private static final String QUERY = "SELECT * FROM \"reservation\"";


    private DataSource dataSource;
    private ReservationHydrator reservationHydrator;
    private Hydrator<Customer> customerHydrator;
    private Hydrator<Room> roomHydrator;
    private static Logger logger = LoggerFactory.getLogger(HotelManager.class);
    private Persister<Reservation> persister;

    public HotelManagerImpl(
            DataSource dataSource,
            ReservationHydrator reservationHydrator,
            Hydrator<Customer> customerHydrator,
            Hydrator<Room> roomHydrator,
            Persister<Reservation> persister
    )
    {
        Objects.requireNonNull(persister);
        Objects.requireNonNull(reservationHydrator);
        Objects.requireNonNull(persister);
        Objects.requireNonNull(roomHydrator);
        Objects.requireNonNull(customerHydrator);

        this.dataSource = dataSource;
        this.reservationHydrator = reservationHydrator;
        this.customerHydrator = customerHydrator;
        this.roomHydrator = roomHydrator;
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
            logger.error(message, e);
            throw new ApplicationException(message, e);
        }
    }

    public List<Reservation> findCustomerReservations(Customer customer) throws ApplicationException
    {
        Objects.requireNonNull(customer);
        Objects.requireNonNull(customer.getId());

        try (Connection conn = dataSource.getConnection()) {
            PreparedStatement statement = conn.prepareStatement(
                    QUERY+ "WHERE \"customer_id\" = ?",
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_UPDATABLE
            );
            statement.setLong(1, customer.getId());
            return executeQueryForMultipleRows(statement.executeQuery());
        } catch (SQLException e) {
            String message = "Find by room was unsuccessful";
            logger.error(message, e);
            throw new ApplicationException(message, e);
        }
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
            logger.error("Reservation couldn't be deleted", e);
        }
    }

    public List<Reservation> findReservationByRoom(Room room)
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
        } catch (SQLException | ApplicationException e) {
            String message = "Find by room was unsuccessful";
            logger.error(message, e);
        }

        return new ArrayList<>();
    }

    @Override
    public void update(Reservation reservation)
    {
        Objects.requireNonNull(reservation);
        Objects.requireNonNull(reservation.getId());
        try {
            persister.update(reservation, reservation.getId());
        } catch(ApplicationException e) {
        }
    }

    public List<Reservation> findReservationsByCustomer(Customer customer)
    {
        Objects.requireNonNull(customer);
        Objects.requireNonNull(customer.getId());

        try (Connection conn = dataSource.getConnection()) {
            PreparedStatement statement = conn.prepareStatement(
                    QUERY+ "WHERE \"customer_id\" = ?",
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_UPDATABLE
            );
            statement.setLong(1, customer.getId());
            return executeQueryForMultipleRows(statement.executeQuery());
        } catch (SQLException | ApplicationException e) {
            String message = "Find by room was unsuccessful";
            logger.error(message, e);
        }

        return new ArrayList<>();
    }

    public List<Reservation> findAll()
    {
        try (Connection conn = dataSource.getConnection()) {
            PreparedStatement statement = conn.prepareStatement(
                    QUERY,
                    ResultSet.TYPE_SCROLL_INSENSITIVE,
                    ResultSet.CONCUR_UPDATABLE
            );
            return executeQueryForMultipleRows(statement.executeQuery());
        } catch (SQLException | ApplicationException e) {
            String message = "Find all was unsuccessful";
            logger.error(message, e);
        }
        return new ArrayList<>();
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
            //logger.error("Find all was unsuccessful", e);
            throw new ApplicationException(String.format("Couln't load data from %s", table), e);
        }
    }

}
