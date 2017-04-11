package hotel.model;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

import hotel.model.database.Hydrator;
import hotel.model.database.Persister;
import hotel.model.database.ReservationHydrator;
import hotel.model.exceptions.ReservationNotFoundException;
import org.junit.Before;
import org.junit.Test;

import javax.sql.DataSource;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.logging.Logger;

public class HotelManagerImplTest extends TestWithDatabase
{

    private HotelManagerImpl manager;

    private RoomManagerImpl rooms;

    private CustomerManagerImpl customers;

    @Before
    public void setUp() throws Exception
    {
        DataSource dataSource = getDataSource();
        Logger logger = null;
        Hydrator<Customer> customerHydrator = new Hydrator<>(Customer.class, logger);
        Hydrator<Room> roomHydrator = new Hydrator<>(Room.class, logger);

        manager = new HotelManagerImpl(
                dataSource,
                new ReservationHydrator(logger),
                customerHydrator,
                roomHydrator,
                logger,
                new Persister<>("reservation", dataSource, logger)
        );

        rooms = new RoomManagerImpl(
                dataSource,
                new Persister<>("room", dataSource, logger),
                roomHydrator,
                logger
        );

        customers = new CustomerManagerImpl(
                dataSource,
                new Persister<>("customer", dataSource, logger),
                customerHydrator,
                logger
        );
    }

    @Test
    public void create() throws Exception
    {
        manager.placeReservation(createReservation(getRoom(4), getCustomer()));
        manager.placeReservation(createReservation(getRoom(7), getCustomer()));

        Reservation reservation = createReservation(getRoom(6), getCustomer());
        manager.placeReservation(reservation);

        assertThat(manager.findAll())
                .hasSize(3);

        assertThat(reservation.getId())
                .isNotNull();
    }

    @Test
    public void createWithNull() throws Exception
    {
        assertThatThrownBy(() -> manager.placeReservation(null))
                .isInstanceOf(NullPointerException.class);

        assertThat(manager.findAll())
                .isEmpty();
    }

    @Test
    public void find() throws Exception
    {
        manager.placeReservation(createReservation(getRoom(666), getCustomer()));

        Reservation reservation = createReservation(getRoom(69), getCustomer());
        manager.placeReservation(reservation);

        assertThat(manager.find(reservation.getId()))
                .isEqualTo(reservation);
    }

    @Test
    public void findForUnknownIdThrowsException() throws Exception
    {
        assertThatThrownBy(() -> manager.find(666))
                .isInstanceOf(ReservationNotFoundException.class);
    }

    @Test
    public void delete() throws Exception
    {
        Reservation reservation = createReservation(getRoom(2), getCustomer());

        manager.placeReservation(reservation);
        manager.delete(reservation);

        assertThat(manager.findAll())
                .isEmpty();
    }

    @Test
    public void deleteOfNotPersistedReservationThrowsException() throws Exception
    {
        assertThatThrownBy(() -> manager.delete(createReservation(getRoom(2), getCustomer())))
                .isInstanceOf(ReservationNotFoundException.class);
    }

    @Test
    public void reservationsForRoom() throws Exception
    {
        Room room = getRoom(2);

        Reservation first = createReservation(room, getCustomer());
        Reservation second = createReservation(room, getCustomer());
        manager.placeReservation(first);
        manager.placeReservation(second);

        assertThat(manager.findReservationByRoom(room))
                .containsExactlyInAnyOrder(first, second);
    }

    private Room getRoom(long number) throws Exception
    {
        Room room = new Room(number, 20, 4, BigDecimal.valueOf(450));
        rooms.create(room);
        return room;
    }

    private Customer getCustomer() throws Exception
    {
        Customer customer = new Customer("Lady Gaga", "Hollywood something", LocalDate.now());
        customers.create(customer);
        return customer;
    }

    private Reservation createReservation(Room room, Customer customer)
    {
        return new Reservation(room, customer, LocalDate.now(), LocalDate.now().plusDays(5));
    }

}