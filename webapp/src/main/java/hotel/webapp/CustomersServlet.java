package hotel.webapp;

import hotel.model.Customer;
import hotel.model.exceptions.ApplicationException;
import hotel.model.exceptions.CustomerHasReservationsException;
import hotel.webapp.forms.CustomerForm;
import hotel.model.exceptions.CustomerNotFoundException;
import hotel.webapp.forms.exceptions.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@WebServlet(CustomersServlet.URL_MAPPING + "*")
public class CustomersServlet extends AbstractServlet
{

    private static final String LIST_JSP = "/templates/customers/list.jsp";
    static final String URL_MAPPING = "/customers/";

    private final static Logger log = LoggerFactory.getLogger(CustomersServlet.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        request.setAttribute("formTarget", "add");

        log.debug("GET ...");

        switch (request.getPathInfo()) {
            case "/update":
                getUpdate(request, response);
                return;
            case "/":
                showCustomersList(request, response);
                return;
        }

        response.sendError(HttpServletResponse.SC_NOT_FOUND);
    }


    void actionAdd(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        try {
            createForm(request).process(request);
            response.sendRedirect(request.getContextPath() + URL_MAPPING);
        } catch (ValidationException e) {
            log.debug("Form data invalid");
            request.setAttribute("error", "You entered invalid data");
            showCustomersList(request, response);
        } catch (ApplicationException e) {
            log.error("Cannot add customer", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    private void getUpdate(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException
    {
        Long id = Long.valueOf(request.getParameter("id"));

        if (id == null) {
            response.sendError(404, "No customer specified");
            return;
        }

        request.setAttribute("formTarget", "update?id=" + id);

        CustomerForm form = createForm(request);

        try {
            form.setId(id);
        } catch (CustomerNotFoundException e) {
            log.debug("Customer not found");
            response.sendRedirect(request.getContextPath() + URL_MAPPING);
            return;
        }

        form.render();
        showCustomersList(request, response);
    }

    void postUpdate(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        Long id = Long.valueOf(request.getParameter("id"));

        if (id == null) {
            response.sendError(404, "No customer specified");
            return;
        }


        request.setAttribute("formTarget", "update?id=" + id);

        CustomerForm form = createForm(request);

        try {
            form.setId(id);
            form.process(request);
            response.sendRedirect(request.getContextPath() + URL_MAPPING);
        } catch (CustomerNotFoundException e) {
            log.debug("Customer not found");
            response.sendRedirect(request.getContextPath() + URL_MAPPING);
        } catch (ValidationException e) {
            log.debug("Form data invalid");
            request.setAttribute("error", "You entered invalid data");
            showCustomersList(request, response);
        } catch (ApplicationException e) {
            log.error("Cannot add customer", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    void actionDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        Long id = Long.valueOf(request.getParameter("id"));

        if (id == null) {
            response.sendError(404, "No customer specified");
            return;
        }

        try {
            Customer customer = getCustomerManager().find(id);

            if (customer == null) {
                response.sendError(404, "Customer not found");
                return;
            }

            getCustomerManager().delete(customer);
            log.debug("redirecting after POST");
            response.sendRedirect(request.getContextPath() + URL_MAPPING);
        } catch (CustomerNotFoundException e) {
            log.error("Cannot remove customer", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        } catch(CustomerHasReservationsException e) {
            String message = "Cannot remove customer with reservations";
            log.error(message);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, message);
        }
    }

    private CustomerForm createForm(HttpServletRequest request)
    {
        return new CustomerForm(request, getCustomerManager());
    }

    /**
     * Stores the list of Customers to request attribute "Customers" and forwards to the JSP to display it.
     */
    private void showCustomersList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        log.debug("showing table of Customers");

        List<Customer> customers = getCustomerManager().findAll();

        Map<Long,Integer> reservations = customers.stream()
                .collect(Collectors.toMap(Customer::getId, c -> getHotelManager().findReservationsByCustomer(c).size()));

        request.setAttribute("customers", customers);
        request.setAttribute("reservations", reservations);
        request.getRequestDispatcher(LIST_JSP).forward(request, response);
        getCustomerManager();
    }

}