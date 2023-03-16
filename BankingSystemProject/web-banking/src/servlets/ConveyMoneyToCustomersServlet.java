package servlets;

import com.google.gson.Gson;
import dtos.DTOLoan;
import general.Transport;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.ServletsUtils;
import utils.SessionUtils;

import java.io.IOException;
import java.util.Arrays;

import static constants.Constants.*;

public class ConveyMoneyToCustomersServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain;charset=UTF-8");
        Transport engine = ServletsUtils.getEngine(getServletContext());
        String username = SessionUtils.getUsername(request);
        Gson gson = new Gson();

        if (username == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
        DTOLoan loan = gson.fromJson(request.getParameter(CHOSEN_LOAN), DTOLoan.class);
        if(request.getParameter(PAYMENT_OPTION).equals("one")) {
            if (!engine.conveyMoneyToLoanLenders(loan)) {
                response.sendError(500, "Error: Unable to convey money to lenders");
            } else {
                response.setStatus(HttpServletResponse.SC_OK);
            }
        }
        else if(request.getParameter(PAYMENT_OPTION).equals("all")) {
            if (!engine.conveyAllMoneyToLoanLenders(loan)) {
                response.sendError(500, "Error: Unable to convey all the money to lenders");
            } else {
                response.setStatus(HttpServletResponse.SC_OK);
            }
        }

    }
}