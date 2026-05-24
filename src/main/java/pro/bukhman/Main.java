package pro.bukhman;

import jakarta.persistence.EntityManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pro.bukhman.util.HibernateUtil;

public class Main {

    private static final Logger log = LogManager.getLogger(Main.class);

    public static void main(String[] args) {

        Runtime.getRuntime().addShutdownHook(new Thread(HibernateUtil::shutdown));
        log.info("Application startup");

        try (EntityManager em = HibernateUtil.createEntityManager()) {
            var tx = em.getTransaction();
            try {
                tx.begin();

                tx.commit();

            } catch (Exception e) {
                if (tx.isActive()) {
                    tx.rollback();
                }
                log.error("Transaction failed in Main.main()", e);
                throw e;
            }
        } catch (Exception e) {
            log.error("Unexpected error while running Main", e);
        }


    }
}