package utils;
import general.Transport;
import jakarta.servlet.ServletContext;
import jakarta.servlet.http.HttpServletRequest;


public class ServletsUtils {

    private static final String ENGINE_ATTRIBUTE_NAME = "engine";
    private static final Object engineLock = new Object();

    public static Transport getEngine(ServletContext servletContext) {

        synchronized (engineLock) {
            if (servletContext.getAttribute(ENGINE_ATTRIBUTE_NAME) == null) {
                servletContext.setAttribute(ENGINE_ATTRIBUTE_NAME, new Transport());
            }
        }
        return (Transport) servletContext.getAttribute(ENGINE_ATTRIBUTE_NAME);
    }
    
}
