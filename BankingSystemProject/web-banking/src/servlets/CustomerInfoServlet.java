package servlets;

import com.google.gson.Gson;
import dtos.DTOcustomer;
import general.Transport;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.ServletsUtils;
import utils.SessionUtils;

import java.io.IOException;
import java.io.PrintWriter;

public class CustomerInfoServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        Transport engine = ServletsUtils.getEngine(getServletContext());
        DTOcustomer customer;
        Gson gson = new Gson();
        String username = SessionUtils.getUsername(request);
        if (username == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }

        synchronized (getServletContext()) {
            customer = engine.getCustomerByHisName(username);
        }

        try (PrintWriter out = response.getWriter()) {
                String json = gson.toJson(customer);
                if (customer != null) {
                    out.println(json);
                    out.flush();
                } else
                    response.setStatus(HttpServletResponse.SC_CONFLICT);
            }
    }
}