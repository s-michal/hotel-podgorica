package hotel;

import java.util.List;

/**
 * Created by xsustera on 8.3.17.
 */
public interface CustomerManager
{

    void create(Customer customer);

    void update(Customer customer);

    Customer find(Long id);

    Customer findByName(String name);

    List<Customer> findAll();

    void delete(Customer customer);
}
