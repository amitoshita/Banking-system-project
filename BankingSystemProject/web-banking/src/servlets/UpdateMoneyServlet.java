package servlets;

import constants.Constants;
import general.Transport;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.ServletsUtils;
import utils.SessionUtils;

import java.io.IOException;

import static constants.Constants.ACTION;
import static constants.Constants.MONEY;

public class UpdateMoneyServlet extends HttpServlet {


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain;charset=UTF-8");
        int money = Integer.parseInt(request.getParameter(MONEY));
        String sign = request.getParameter(ACTION);
        String usernameFromSession = SessionUtils.getUsername(request);
        Transport engine = ServletsUtils.getEngine(getServletContext());

        if (usernameFromSession == null) { //user is not logged in yet
            response.setStatus(HttpServletResponse.SC_CONFLICT);
        }
        else {
            if(sign.equals("+")) {
                engine.addMoneyToCustomer(usernameFromSession, money);
            }
            else if(sign.equals("-")){
                engine.minusMoneyToCustomer(usernameFromSession, money);
            }
            response.setStatus(HttpServletResponse.SC_OK);
        }
    }
}
