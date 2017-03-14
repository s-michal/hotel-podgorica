package hotel;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by xsustera on 8.3.17.
 */
public class CustomerManagerImplTest {

    CustomerManagerImpl customerManager;

    @Before
    public void setUp() throws Exception {
        customerManager = new CustomerManagerImpl();
        customerManager.create(new Customer("Tomas", "Jirova"));
        customerManager.create(new Customer("Dominik", "Molakova"));
    }

    @Test
    public void create() throws Exception {
        Customer petr = new Customer("Petr", "Vitezna");
        customerManager.create(petr);
        assertSame("Customer is not in a database", petr, customerManager.findByName("Petr"));
    }

    @Test
    public void update() throws Exception {
        Customer tomas = new Customer("Tomas", "Konradova");
        customerManager.update(tomas);
        assertSame("Address in not same", tomas.getAddress(), customerManager.findByName("Tomas").getAddress());
    }

    @Test
    public void find() throws Exception {
        assertNotNull("Customer has not ID", customerManager.findByName("Tomas").getId());
    }

    @Test
    public void findByName() throws Exception {
        Customer tomas = customerManager.findByName("Tomas");
        assertSame("Tomas", tomas.getName());
    }

    @Test
    public void findAll() throws Exception {
        List<Customer> all = customerManager.findAll();
        assertEquals(2, all.size());
    }

    @Test
    public void delete() throws Exception {
        customerManager.delete(customerManager.findByName("Tomas"));
        List<Customer> all = customerManager.findAll();
        assertEquals(1, all.size());
        assertSame("Dominik", all.get(0).getName());
    }

}