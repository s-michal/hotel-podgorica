package hotel.model;

import hotel.model.database.Hydrator;
import hotel.model.database.Persister;
import org.junit.Before;
import org.junit.Test;

import javax.sql.DataSource;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

public class CustomerManagerImplTest extends TestWithDatabase
{

    CustomerManagerImpl customerManager;

    @Before
    public void setUp() throws Exception
    {
        DataSource dataSource = getDataSource();
        customerManager = new CustomerManagerImpl(
                dataSource,
                new Persister<>("customer", dataSource),
                new Hydrator<>(Customer.class)
        );
    }


    @Test
    public void testingCreateAndFindByName() throws Exception
    {
        Customer petr = new Customer("Petr", "Vitezna", LocalDate.of(1977, 8, 21));
        customerManager.create(petr);
        assertThat(customerManager.findByName("Petr")).contains(petr);
    }

    @Test
    public void testingFindAll() throws Exception
    {
        Customer tomas = new Customer("Tomas", "Konradova", LocalDate.of(1965, 4, 14));
        Customer petr = new Customer("Petr", "Vitezna", LocalDate.of(1977, 8, 21));
        customerManager.create(tomas);
        customerManager.create(petr);
        List<Customer> allCustomer = new ArrayList<>();
        allCustomer.add(tomas);
        allCustomer.add(petr);
        assertThat(customerManager.findAll()).isEqualTo(allCustomer);
    }

    @Test
    public void testingFind() throws Exception
    {
        Customer tomas = new Customer("Tomas", "Konradova", LocalDate.of(1965, 4, 14));
        customerManager.create(tomas);
        assertThat(tomas.getId()).isNotNull();
        assertThat(customerManager.find(tomas.getId())).isEqualTo(tomas);
    }

    @Test
    public void testingUpdate() throws Exception
    {
        Customer tomas = new Customer("Tomas", "Konradova", LocalDate.of(1965, 4, 14));
        customerManager.create(tomas);
        tomas.setAddress("Vitezna");
        customerManager.update(tomas);

        tomas = customerManager.find(tomas.getId());
        assertThat(tomas.getAddress()).isEqualTo("Vitezna");
    }

    @Test
    public void testingDelete() throws Exception
    {
        Customer tomas = new Customer("Tomas", "Konradova", LocalDate.of(1965, 4, 14));
        customerManager.create(tomas);
        customerManager.delete(tomas);
        assertThat(customerManager.findAll()).hasSize(0);
    }

    @Test
    public void testingDeleteUnsavedCustomerThrowsException() throws Exception
    {
        Customer tomas = new Customer("Tomas", "Konradova", LocalDate.of(1965, 4, 14));
        assertThatThrownBy(() -> customerManager.delete(tomas)).isInstanceOf(NullPointerException.class);
    }

    @Test
    public void testingSameCustomer() throws Exception
    {
        Customer tomas = new Customer("Tomas", "Konradova", LocalDate.of(1965, 4, 14));
        customerManager.create(tomas);
        assertThatThrownBy(() -> customerManager.create(tomas)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void testingCreateNullObject() throws Exception
    {
        assertThatThrownBy(() -> customerManager.create(null)).isInstanceOf(NullPointerException.class);
    }
}