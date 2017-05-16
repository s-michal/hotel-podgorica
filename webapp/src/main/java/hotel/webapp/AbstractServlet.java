package hotel.webapp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

abstract class AbstractServlet extends BaseServlet
{
    private final static Logger log = LoggerFactory.getLogger(AbstractServlet.class);

    abstract void actionAdd(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;

    abstract void postUpdate(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;

    abstract void actionDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        request.setAttribute("formTarget", "add");
        //support non-ASCII characters in form
        request.setCharacterEncoding("utf-8");
        //action specified by pathInfo
        String action = request.getPathInfo();
        log.debug("POST ... {}", action);
        switch (action) {
            case "/add":
                actionAdd(request, response);
                return;
            case "/delete":
                actionDelete(request, response);
                return;
            case "/update":
                postUpdate(request, response);
                return;
            default:
                log.error("Unknown action " + action);
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Unknown action " + action);
        }
    }
}
