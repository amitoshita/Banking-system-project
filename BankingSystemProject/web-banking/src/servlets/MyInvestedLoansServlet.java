package servlets;

import com.google.gson.Gson;
import dtos.DTOLoan;
import general.Transport;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import javafx.collections.ObservableList;
import utils.ServletsUtils;
import utils.SessionUtils;

import java.io.IOException;
import java.io.PrintWriter;

public class MyInvestedLoansServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json");
        Transport engine = ServletsUtils.getEngine(getServletContext());
        ObservableList<DTOLoan> loansList;
        Gson gson = new Gson();
        String username = SessionUtils.getUsername(request);

        if (username == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }

        synchronized (getServletContext()) {
            loansList = engine.getObservableLendersListByName(username);
        }

        try (PrintWriter out = response.getWriter()) {
            String json = gson.toJson(loansList);
            if(loansList != null) {
                out.println(json);
                out.flush();
                response.setStatus(HttpServletResponse.SC_OK);
            }
            else
                response.setStatus(HttpServletResponse.SC_CONFLICT);
        }
    }
}
