package hotel.gui.customers;

import hotel.gui.BaseModel;
import hotel.model.*;
import hotel.model.exceptions.ApplicationException;
import hotel.model.exceptions.CustomerHasReservationsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.*;

public class CustomersModel extends BaseModel<Customer>
{

    private CustomerManager manager;
    private HotelManager hotelManager;
    private List<Customer> customers;

    private static Logger logger = LoggerFactory.getLogger(CustomersModel.class);

    CustomersModel(CustomerManager manager, HotelManager hotelManager)
    {
        super(
                new String[] {"ID", "Name", "Birth date", "Address"},
                new Class<?>[] {Long.class, String.class, LocalDate.class, String.class}
        );

        Objects.requireNonNull(manager);
        Objects.requireNonNull(hotelManager);
        this.manager = manager;
        this.hotelManager = hotelManager;
    }

    @Override
    public int getRowCount()
    {
        return getCustomers().size();
    }

    @Override
    protected Object getColumnValue(int rowIndex, int columnIndex)
    {
        Customer customer = getRow(rowIndex);
        switch (columnIndex) {
            case 0:
                return customer.getId();
            case 1:
                return customer.getName();
            case 2:
                return customer.getBirthDate();
            case 3:
                return customer.getAddress();
        }
        throw new IllegalArgumentException("columnIndex");
    }

    @Override
    protected Customer getRow(int rowIndex)
    {
        return getCustomers().get(rowIndex);
    }

    public void invalidate()
    {
        customers = null;
        getCustomers();
        fireTableDataChanged();
    }

    public void deleteCustomer(Customer customer) throws CustomerHasReservationsException
    {
        manager.delete(customer);
        invalidate();
    }

    public boolean hasReservations(Customer customer)
    {
        return hotelManager.findReservationsByCustomer(customer).size() > 0;
    }

    public void createCustomer(Customer customer) throws ApplicationException
    {
        manager.create(customer);
        invalidate();
    }

    public void updateCustomer(Customer customer) throws ApplicationException
    {
        manager.update(customer);
        invalidate();
    }

    private List<Customer> getCustomers()
    {
        if (customers == null) {
            logger.debug("Loading customers");
            customers = manager.findAll();
        }

        return customers;
    }

}