package servlets;

import exceptions.fileCategoriesException;
import exceptions.fileCustomersNamesException;
import exceptions.fileDivideYazPaymentException;
import exceptions.fileExtentionException;
import general.Transport;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import utils.ServletsUtils;
import utils.SessionUtils;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.Scanner;

@WebServlet("/upload-file")
@MultipartConfig(fileSizeThreshold = 1024 * 1024, maxFileSize = 1024 * 1024 * 5, maxRequestSize = 1024 * 1024 * 5 * 5)
public class FileUploadServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Transport engine = ServletsUtils.getEngine(getServletContext());
        String usernameFromSession = SessionUtils.getUsername(request);
        response.setContentType("text/xml");
        Part file = request.getPart("file1");
        InputStream filecontent = file.getInputStream();
        try {
            engine.loadNewXMLFileEX3(filecontent, usernameFromSession);
            response.setStatus(HttpServletResponse.SC_OK);
        } catch (FileNotFoundException e) {
            response.sendError(500, "File not found " + e.getMessage());
        } catch (fileExtentionException e) {
            response.sendError(500, "File not valid extention " + e.getMessage());
        } catch (JAXBException | fileDivideYazPaymentException | fileCustomersNamesException e) {
            response.sendError(500, "Something went wrong... OOPS " + e.getMessage());
        }

    }

}
