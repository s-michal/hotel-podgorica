package hotel.webapp;

import hotel.model.Customer;
import hotel.model.CustomerManager;
import hotel.model.exceptions.ApplicationException;
import hotel.webapp.forms.CustomerForm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(CustomersServlet.URL_MAPPING + "*")
public class CustomersServlet extends HttpServlet {

    private static final String LIST_JSP = "/templates/customers/list.jsp";
    public static final String URL_MAPPING = "/customers/";

    private final static Logger log = LoggerFactory.getLogger(CustomersServlet.class);

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("GET ...");

        if(!request.getPathInfo().equals("/")) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        showCustomersList(request, response);
    }


    private void actionAdd(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        Customer customer = CustomerForm.process(request);

        if(customer == null) {
            log.debug("Form data invalid");
            request.setAttribute("error", "You entered invalid data");
            showCustomersList(request, response);
            return;
        }

        try {
            getCustomerManager().create(customer);
            response.sendRedirect(request.getContextPath()+URL_MAPPING);
        } catch (ApplicationException e) {
            log.error("Cannot add customer", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    private void actionDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        Long id = Long.valueOf(request.getParameter("id"));

        if(id == null) {
            response.sendError(404, "No customer specified");
            return;
        }

        try {
            Customer customer = getCustomerManager().find(id);

            if(customer == null) {
                response.sendError(404, "Customer not found");
                return;
            }

            getCustomerManager().delete(customer);
            log.debug("redirecting after POST");
            response.sendRedirect(request.getContextPath()+URL_MAPPING);
        } catch (ApplicationException e) {
            log.error("Cannot remove customer", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //support non-ASCII characters in form
        request.setCharacterEncoding("utf-8");
        //action specified by pathInfo
        String action = request.getPathInfo();
        log.debug("POST ... {}",action);
        switch (action) {
            case "/add":
                actionAdd(request, response);
                return;
            case "/delete":
                actionDelete(request, response);
                return;
            case "/update":
                //TODO
                return;
            default:
                log.error("Unknown action " + action);
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Unknown action " + action);
        }
    }

    private CustomerManager getCustomerManager() {
        return (CustomerManager) getServletContext().getAttribute("customerManager");
    }

    /**
     * Stores the list of Customers to request attribute "Customers" and forwards to the JSP to display it.
     */
    private void showCustomersList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            log.debug("showing table of Customers");
            request.setAttribute("customers", getCustomerManager().findAll());
            request.getRequestDispatcher(LIST_JSP).forward(request, response);
        } catch (ApplicationException e) {
            log.error("There was an error", e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

}