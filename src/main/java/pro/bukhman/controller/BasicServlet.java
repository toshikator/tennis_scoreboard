package pro.bukhman.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.EntityManagerFactory;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pro.bukhman.config.AppInitializer;

import java.io.IOException;

public class BasicServlet extends HttpServlet {
    private static Logger logger;
    protected final ObjectMapper objectMapper = new ObjectMapper();
    protected EntityManagerFactory emf;

    @Override
    public void init() throws ServletException {
        logger = LogManager.getLogger(BasicServlet.class);
        logger.info("Initializing {}", getClass().getSimpleName());
        Object attribute = getServletContext()
                .getAttribute(AppInitializer.ATTR_ENTITY_MANAGER_FACTORY);

        if (!(attribute instanceof EntityManagerFactory entityManagerFactory)) {
            throw new ServletException("EntityManagerFactory was not initialized");
        }

        this.emf = entityManagerFactory;
        logger.info("EntityManagerFactory obtained from ServletContext");
    }

    protected void sendJson(HttpServletResponse resp, int status, Object body)
            throws IOException {

        resp.setStatus(status);
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");

        objectMapper.writeValue(resp.getWriter(), body);
    }
}
