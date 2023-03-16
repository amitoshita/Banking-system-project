package servlets;

import com.google.gson.Gson;
import general.Transport;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.ServletsUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;

public class CustomersNamesListServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //returning JSON objects, not HTML
        response.setContentType("application/json");
        try (PrintWriter out = response.getWriter()) {
            Gson gson = new Gson();
            Transport engine = ServletsUtils.getEngine(getServletContext());
            Set<String> customersList = engine.getCustomersNames();
            String json = gson.toJson(customersList);
            out.println(json);
            out.flush();
            response.setStatus(HttpServletResponse.SC_OK);
        }
    }
}
