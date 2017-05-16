package hotel.gui;

import hotel.model.*;
import hotel.model.database.DatasourceFactory;
import hotel.model.database.Hydrator;
import hotel.model.database.Persister;
import hotel.model.database.ReservationHydrator;

import javax.sql.DataSource;

public class Context
{

    private DataSource dataSource;
    private Hydrator<Customer> customerHydrator;
    private Hydrator<Room> roomHydrator;

    private CustomerManager customerManager;
    private RoomManager roomManager;
    private HotelManager hotelManager;

    public HotelManager getHotelManager()
    {
        if(hotelManager == null) {
            hotelManager = new HotelManagerImpl(
                    getDataSource(),
                    new ReservationHydrator(),
                    getCustomerHydrator(),
                    getRoomHydrator(),
                    new Persister<>("reservation", getDataSource())
            );
        }

        return hotelManager;
    }

    public CustomerManager getCustomerManager()
    {
        if(customerManager == null) {
            customerManager = new CustomerManagerImpl(
                    getDataSource(),
                    new Persister<>("customer", getDataSource()),
                    getCustomerHydrator()
            );
        }

        return customerManager;
    }

    public RoomManager getRoomManager()
    {
        if(roomManager == null) {
            roomManager = new RoomManagerImpl(
                    getDataSource(),
                    new Persister<>("room", getDataSource()),
                    getRoomHydrator()
            );
        }

        return roomManager;
    }

    private DataSource getDataSource()
    {
        if(dataSource == null) {
           dataSource = DatasourceFactory.create("memory:gui-app", new String[] {
                    "/sql/up.sql",
                    "/sql/data.sql",
            });
        }

        return dataSource;
    }

    private Hydrator<Customer> getCustomerHydrator()
    {
        if(customerHydrator == null) {
            customerHydrator = new Hydrator<>(Customer.class);
        }

        return customerHydrator;
    }

    private Hydrator<Room> getRoomHydrator()
    {
        if(roomHydrator == null) {
            roomHydrator = new Hydrator<>(Room.class);
        }

        return roomHydrator;
    }

}
