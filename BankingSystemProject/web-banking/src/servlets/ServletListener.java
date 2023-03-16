package servlets;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import utils.ServletsUtils;

@WebListener
public class ServletListener implements ServletContextListener {

        @Override
        public void contextInitialized(ServletContextEvent servletContextEvent) {
            System.out.println("My web app is being initialized :)");
            ServletsUtils.getEngine(servletContextEvent.getServletContext());
        }

        @Override
        public void contextDestroyed(ServletContextEvent servletContextEvent) {

            System.out.println("My web app is being destroyed ... :(");
        }
}