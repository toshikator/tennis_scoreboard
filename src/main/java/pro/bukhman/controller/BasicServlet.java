package pro.bukhman.controller;

import jakarta.persistence.EntityManagerFactory;
import jakarta.servlet.http.HttpServlet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pro.bukhman.config.AppInitializer;

public class BasicServlet extends HttpServlet {
    private static Logger logger;

    protected EntityManagerFactory emf;

    @Override
    public void init() {
        logger = LogManager.getLogger(BasicServlet.class);
        logger.info("Initializing {}", getClass().getSimpleName());
        try {
            emf = (EntityManagerFactory) getServletContext().getAttribute(AppInitializer.ATTR_ENTITY_MANAGER_FACTORY);
            if (emf == null) {
                logger.warn("EntityManagerFactory not found under key 'entityManagerFactory'. AppInitializer uses key '{}'", AppInitializer.ATTR_ENTITY_MANAGER_FACTORY);
            } else {
                logger.info("EntityManagerFactory obtained from ServletContext");
            }
        } catch (Exception e) {
            logger.error("Unexpected error during servlet init", e);
        }
    }
}
