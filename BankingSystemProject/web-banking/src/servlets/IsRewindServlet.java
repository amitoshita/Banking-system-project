package servlets;

import com.google.gson.Gson;
import general.Transport;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import utils.ServletsUtils;
import utils.SessionUtils;

import java.io.IOException;

import static constants.Constants.*;

public class IsRewindServlet  extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/plain;charset=UTF-8");
        Transport engine = ServletsUtils.getEngine(getServletContext());
        String username = SessionUtils.getUsername(request);
        Gson gson = new Gson();

        if (username == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
        if(request.getParameter(USERTYPE).equals("admin")){
            if(request.getParameter(REWIND).equals("true")){
                engine.setRewind(true);
            }
            else {
                engine.setRewind(false);
            }
        }
        else if(request.getParameter(USERTYPE).equals("customer")){
            String json = gson.toJson(engine.getRewind());
            response.getWriter().println(json);
        }
    }
}
