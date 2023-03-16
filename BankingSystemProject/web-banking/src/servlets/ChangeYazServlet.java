package servlets;

import com.google.gson.Gson;
import general.Transport;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.ServletsUtils;
import utils.SessionUtils;

import java.io.IOException;

import static constants.Constants.ACTION;
import static constants.Constants.AMOUNT;

public class ChangeYazServlet  extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain;charset=UTF-8");
        Transport engine = ServletsUtils.getEngine(getServletContext());
        String username = SessionUtils.getUsername(request);
        Gson gson = new Gson();
        if (username == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
        if(request.getParameter(ACTION).equals("add")) {
            engine.addLoanYaz();
        }
        else if(request.getParameter(ACTION).equals("minus")){
            //to create minus yaz func
        }
        String json = gson.toJson(engine.getLoanYaz());
        response.getWriter().println(json);
        response.setStatus(HttpServletResponse.SC_OK);
    }
}
