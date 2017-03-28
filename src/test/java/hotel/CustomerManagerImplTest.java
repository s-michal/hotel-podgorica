package hotel;

import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

public class CustomerManagerImplTest
{

    CustomerManagerImpl customerManager;

    @Before
    public void setUp() throws Exception
    {
        customerManager = new CustomerManagerImpl();
    }


    @Test
    public void testingCreateAndFindByName() throws Exception
    {
        Customer petr = new Customer("Petr", "Vitezna", LocalDate.of(1977, 8, 21));
        customerManager.create(petr);
        assertThat(customerManager.findByName("Petr")).isEqualTo(petr);
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
        assertThat(customerManager.findByName("Tomas").getAddress()).isSameAs("Vitezna");
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
    public void testingSameCustomer() throws Exception
    {
        Customer tomas = new Customer("Tomas", "Konradova", LocalDate.of(1965, 4, 14));
        customerManager.create(tomas);
        Customer secondTomas = new Customer("Tomas", "Konradova", LocalDate.of(1965, 4, 14));
        assertThatThrownBy(() -> customerManager.create(secondTomas)).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    public void testingCreateNullObject() throws Exception
    {
        assertThatThrownBy(() -> customerManager.create(null)).isInstanceOf(NullPointerException.class);
    }
}