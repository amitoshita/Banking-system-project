package servlets;

import com.google.gson.Gson;
import dtos.DTOLoan;
import dtos.DTOcustomer;
import general.Transport;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.ServletsUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Set;

import static constants.Constants.REWIND;
import static constants.Constants.YAZ;

public class CustomersListServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //returning JSON objects, not HTML
        response.setContentType("application/json");
        try (PrintWriter out = response.getWriter()) {
            Gson gson = new Gson();
            Transport engine = ServletsUtils.getEngine(getServletContext());
            if(request.getParameter(REWIND).equals("false")) {
                List<DTOcustomer> customers1 = engine.getDTOCustomers().getAllCustomers();
                String json = gson.toJson(customers1);
                out.println(json);
                out.flush();
            }
            else if (request.getParameter(REWIND).equals("true")){
                Integer yaz = Integer.parseInt(request.getParameter(YAZ));
                List<DTOcustomer> customers2 = engine.getCustomersListState(yaz);
                String json = gson.toJson(customers2);
                out.println(json);
                out.flush();
            }
            response.setStatus(HttpServletResponse.SC_OK);
        }
    }
}
