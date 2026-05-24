package pro.bukhman;

import jakarta.persistence.EntityManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pro.bukhman.model.entity.Player;
import pro.bukhman.util.HibernateUtil;

public class PlayersAdd {

    private static final Logger log = LogManager.getLogger(PlayersAdd.class);

    public static void main(String[] args) {
        Runtime.getRuntime().addShutdownHook(new Thread(HibernateUtil::shutdown));
        log.info("Application startup: seeding players");
        try (EntityManager em = HibernateUtil.createEntityManager()) {
            var tx = em.getTransaction();
            try {
                tx.begin();
                em.persist(new Player("Roger", "Federer"));
                em.persist(new Player("Rafael", "Nadal"));
                em.persist(new Player("Rod", "Laver"));
                em.persist(new Player("Steffi", "Graf"));
                em.persist(new Player("Martina", "Navratilova"));
                em.persist(new Player("Pete", "Sampras"));
                em.persist(new Player("Bjorn", "Borg"));
                em.persist(new Player("Margaret", "Court"));
                em.persist(new Player("Chris", "Evert"));
                em.persist(new Player("Billie Jean", "King"));
                em.persist(new Player("Don", "Budge"));
                em.persist(new Player("Andre", "Agassi"));
                tx.commit();
                log.info("Players persisted successfully");
            } catch (Exception e) {
                if (tx.isActive()) {
                    tx.rollback();
                }
                log.error("Transaction failed in PlayersAdd.main()", e);
                throw e;
            }
        } catch (Exception e) {
            log.error("Unexpected error while running PlayersAdd", e);
        }


    }
}