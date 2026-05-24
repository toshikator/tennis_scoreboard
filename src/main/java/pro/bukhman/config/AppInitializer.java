package pro.bukhman.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import jakarta.persistence.EntityManagerFactory;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import pro.bukhman.matchStorage.OngoingMatchStorage;
import pro.bukhman.util.HibernateUtil;
import pro.bukhman.util.PropertiesReader;

import java.util.HashMap;
import java.util.Map;

@WebListener
public class AppInitializer implements ServletContextListener {

    public static final String ATTR_ENTITY_MANAGER_FACTORY = "ENTITY_MANAGER_FACTORY";
    public static final String ATTR_HIBERNATE_CONFIG = "HIBERNATE_CONFIG";
    private static final Logger logger = LogManager.getLogger(AppInitializer.class);

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext ctx = sce.getServletContext();
        ctx.log("Initializing JPA EntityManagerFactory");
        OngoingMatchStorage storage = new OngoingMatchStorage();
        sce.getServletContext().setAttribute("ongoingMatchStorage", storage);
        try {
            logger.debug("Starting JPA EntityManagerFactory initialization");

            PropertiesReader pr = PropertiesReader.getInstance();

            String url = pr.get("db.url") + ":" + pr.get("db.port") + "/" + pr.get("db.name");
            Map<String, Object> hibernateConfig = new HashMap<>();
            hibernateConfig.put("jakarta.persistence.jdbc.url", url);
            hibernateConfig.put("jakarta.persistence.jdbc.user", pr.get("db.username"));
            hibernateConfig.put("jakarta.persistence.jdbc.password", pr.get("db.password"));
            hibernateConfig.put("jakarta.persistence.jdbc.driver", pr.get("db.driver"));
            hibernateConfig.put("hibernate.hbm2ddl.auto", pr.get("hibernate.hbm2ddl.auto"));
            hibernateConfig.put("hibernate.show_sql", pr.get("hibernate.show_sql"));
            hibernateConfig.put("hibernate.format_sql", pr.get("hibernate.format_sql"));
            hibernateConfig.put("hibernate.jdbc.time_zone", pr.get("hibernate.jdbc.time_zone"));
            hibernateConfig.put("hibernate.cache.use_second_level_cache", pr.get("hibernate.cache.use_second_level_cache"));
            hibernateConfig.put("hibernate.generate_statistics", pr.get("hibernate.generate_statistics"));
            hibernateConfig.put("hibernate.default_batch_fetch_size", pr.get("hibernate.default_batch_fetch_size"));

            ctx.setAttribute(ATTR_HIBERNATE_CONFIG, hibernateConfig);
            HibernateUtil.setConfig(hibernateConfig);
            EntityManagerFactory emf = HibernateUtil.getEntityManagerFactory();
            ctx.setAttribute(ATTR_ENTITY_MANAGER_FACTORY, emf);
            logger.info("EntityManagerFactory initialized successfully");
            ctx.setAttribute("app.root", ctx.getRealPath("/"));

        } catch (Exception e) {
            logger.error("Failed to initialize EntityManagerFactory", e);
            throw new IllegalStateException("Cannot initialize JPA EntityManagerFactory", e);
        }
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ServletContext context = sce.getServletContext();
        Object attribute = context.getAttribute(ATTR_ENTITY_MANAGER_FACTORY);
        if (attribute instanceof EntityManagerFactory emf) {
            if (emf.isOpen()) {
                logger.info("Closing EntityManagerFactory");
                HibernateUtil.shutdown();
                context.log("EntityManagerFactory closed");
            }
        }
        sce.getServletContext().removeAttribute("ongoingMatchStorage");
    }
}
