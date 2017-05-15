package hotel.webapp.forms;

import hotel.model.Customer;
import hotel.model.CustomerManager;
import hotel.model.exceptions.ApplicationException;
import hotel.webapp.forms.exceptions.CustomerNotFoundException;
import hotel.webapp.forms.exceptions.ValidationException;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class CustomerForm
{

    private HttpServletRequest request;
    private CustomerManager manager;

    private Customer customer;

    public CustomerForm(HttpServletRequest request, CustomerManager manager)
    {
        this.request = request;
        this.manager = manager;
    }

    public void setId(long id) throws CustomerNotFoundException, ApplicationException
    {
        customer = manager.find(id);

        if (customer == null) {
            throw new CustomerNotFoundException();
        }
    }

    public void render()
    {
        if (this.customer == null) {
            return;
        }

        request.setAttribute("name", customer.getName());
        request.setAttribute("address", customer.getAddress());
        request.setAttribute("birthDate", customer.getBirthDate());
    }

    public void process(HttpServletRequest request) throws ValidationException, ApplicationException
    {
        String name = request.getParameter("name");
        String address = request.getParameter("address");

        request.setAttribute("name", name);
        request.setAttribute("address", address);

        LocalDate birthDate;
        try {
            birthDate = LocalDate.parse(request.getParameter("birthDate"));
        } catch (DateTimeParseException e) {
            throw new ValidationException(e);
        }

        request.setAttribute("birthDate", birthDate);

        try {
            if (this.customer == null) {
                create(name, address, birthDate);
                return;
            }

            update(name, address, birthDate);
        } catch (IllegalArgumentException e) {
            throw new ValidationException(e);
        }
    }

    private void create(String name, String address, LocalDate birthDate) throws ApplicationException
    {
        manager.create(new Customer(name, address, birthDate));
    }

    private void update(String name, String address, LocalDate birthDate) throws ApplicationException, ValidationException
    {
        customer.update(name, address, birthDate);
        manager.update(customer);
    }

}
