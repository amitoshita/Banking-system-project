package servlets;

import com.google.gson.Gson;
import dtos.DTOlender;
import general.Transport;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.ServletsUtils;
import utils.SessionUtils;

import java.io.IOException;

import static constants.Constants.CHOSEN_PART_TO_BUY;
import static constants.Constants.CHOSEN_PART_TO_SELL;

public class SellLoanPartServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain;charset=UTF-8");
        Transport engine = ServletsUtils.getEngine(getServletContext());
        String username = SessionUtils.getUsername(request);
        Gson gson = new Gson();
        if (username == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }

        DTOlender lenderPart = gson.fromJson(request.getParameter(CHOSEN_PART_TO_SELL), DTOlender.class);
        engine.sellLoanPart(lenderPart);
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
