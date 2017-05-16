package hotel.webapp;

import hotel.model.*;
import hotel.model.database.DatasourceFactory;
import hotel.model.database.Hydrator;
import hotel.model.database.Persister;
import hotel.model.database.ReservationHydrator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.sql.DataSource;

@WebListener
public class StartListener implements ServletContextListener
{


    private final static Logger log = LoggerFactory.getLogger(StartListener.class);

    @Override
    public void contextInitialized(ServletContextEvent ev)
    {
        log.info("Webapp initialized");

        ServletContext context = ev.getServletContext();

        DataSource ds = DatasourceFactory.create("memory:main", new String[] {
                "/sql/up.sql",
                "/sql/data.sql",
        });

        Hydrator<Customer> customerHydrator = new Hydrator<>(Customer.class);
        Hydrator<Room> roomHydrator = new Hydrator<>(Room.class);


        context.setAttribute("customerManager", createCustomerManager(ds, customerHydrator));
        context.setAttribute("roomManager", createRoomManager(ds, roomHydrator));
        context.setAttribute("hotelManager", createHotelManager(ds, customerHydrator, roomHydrator));

        log.info("Managers initialized");
    }

    @Override
    public void contextDestroyed(ServletContextEvent ev)
    {
        log.info("Webapp is shutting down");
    }

    private CustomerManager createCustomerManager(DataSource ds, Hydrator<Customer> hydrator)
    {
        return new CustomerManagerImpl(ds, new Persister<>("customer", ds), hydrator);
    }

    private RoomManager createRoomManager(DataSource ds, Hydrator<Room> hydrator)
    {
        return new RoomManagerImpl(ds, new Persister<>("room", ds), hydrator);
    }


    private HotelManager createHotelManager(DataSource ds, Hydrator<Customer> customerHydrator, Hydrator<Room> roomHydrator)
    {
        return new HotelManagerImpl(ds, new ReservationHydrator(), customerHydrator, roomHydrator, new Persister<>("reservation", ds));
    }

}
