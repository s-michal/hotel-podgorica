package hotel;

import java.util.List;

/**
 * Created by xsustera on 8.3.17.
 */
public interface CustomerManager
{

    void create(Customer customer) throws ApplicationException;

    void update(Customer customer) throws ApplicationException;

    Customer find(Long id);

    List<Customer> findByName(String name);

    List<Customer> findAll();

    void delete(Customer customer);
}
