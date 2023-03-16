package servlets;

import com.google.gson.Gson;
import dtos.DTOLoan;
import general.Transport;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.ServletsUtils;
import utils.SessionUtils;

import java.util.Arrays;

import static constants.Constants.*;

public class ScrumbleServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("text/plain;charset=UTF-8");
        Transport engine = ServletsUtils.getEngine(getServletContext());
        String username = SessionUtils.getUsername(request);
        Gson gson = new Gson();

        if (username == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
        DTOLoan[] loans = gson.fromJson(request.getParameter(CHOSEN_LOANS), DTOLoan[].class);
        engine.divideTheLoansMoney(Arrays.asList(loans), Integer.parseInt(request.getParameter(AMOUNT)), username, Integer.parseInt(request.getParameter(MAX_PRECENT)));
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
