package hotel.model;

import hotel.model.exceptions.ApplicationException;
import hotel.model.exceptions.CustomerNotFoundException;

import java.util.List;

public interface CustomerManager
{

    void create(Customer customer) throws ApplicationException;

    void update(Customer customer) throws ApplicationException;

    Customer find(Long id) throws CustomerNotFoundException;

    List<Customer> findByName(String name) throws ApplicationException;

    List<Customer> findAll();

    void delete(Customer customer);
}
