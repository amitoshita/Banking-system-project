package servlets;

import general.Transport;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.ServletsUtils;
import utils.SessionUtils;

import java.io.IOException;

import static constants.Constants.*;

public class CreateNewLoanServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain;charset=UTF-8");
        String loanID = request.getParameter(LOAN_ID);
        String category = request.getParameter(CATEGORY);
        int amount = Integer.parseInt(request.getParameter(AMOUNT));
        int totalYaz =  Integer.parseInt(request.getParameter(TOTAL_YAZ));
        int paysEveryYaz =  Integer.parseInt(request.getParameter(PAYS_EVERY_YAZ));
        int interest =  Integer.parseInt(request.getParameter(INTEREST));

        String usernameFromSession = SessionUtils.getUsername(request);
        Transport engine = ServletsUtils.getEngine(getServletContext());

        if (usernameFromSession == null) { //user is not logged in yet
            response.setStatus(HttpServletResponse.SC_CONFLICT);
        }
        else if (engine.isLoanIDExist(loanID)) {
            String errorMessage = "Loan ID " + loanID + " already exists. Please enter a different loan ID.";
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getOutputStream().print(errorMessage);
        }
        else
        {
            engine.createNewLoan(usernameFromSession, loanID, category, amount, totalYaz, paysEveryYaz, interest);
            response.setStatus(HttpServletResponse.SC_OK);
        }

    }

}
