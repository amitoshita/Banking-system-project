package servlets;

import com.google.gson.Gson;
import dtos.DTOLoan;
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

public class LoansListServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        //returning JSON objects, not HTML
        response.setContentType("application/json");
        try (PrintWriter out = response.getWriter()) {
            Gson gson = new Gson();
            Transport engine = ServletsUtils.getEngine(getServletContext());
            if(request.getParameter(REWIND).equals("false")) {
                List<DTOLoan> loanList = engine.getDTOLoans().getDTOloanList();
                String json = gson.toJson(loanList);
                out.println(json);
                out.flush();
            }
            else if (request.getParameter(REWIND).equals("true")){
                Integer yaz = Integer.parseInt(request.getParameter(YAZ));
                List<DTOLoan> loanList2 = engine.getLoansListState(yaz);
                String json = gson.toJson(loanList2);
                out.println(json);
                out.flush();
            }

            response.setStatus(HttpServletResponse.SC_OK);
        }
    }
}
