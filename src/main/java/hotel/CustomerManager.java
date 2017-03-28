package hotel;

import java.util.List;

/**
 * Created by xsustera on 8.3.17.
 */
public interface CustomerManager
{

    void create(Customer customer) throws ApplicationException;

    void update(Customer customer) throws ApplicationException;

    Customer find(Long id) throws ApplicationException;

    List<Customer> findByName(String name) throws ApplicationException;

    List<Customer> findAll() throws ApplicationException;

    void delete(Customer customer);
}
