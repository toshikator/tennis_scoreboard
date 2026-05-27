package pro.bukhman;

import jakarta.persistence.EntityManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pro.bukhman.model.entity.Match;
import pro.bukhman.model.entity.Player;
import pro.bukhman.util.HibernateUtil;

import java.time.LocalDate;

public class MatchesAdd {

    private static final Logger log = LogManager.getLogger(MatchesAdd.class);

    public static void main(String[] args) {

        Runtime.getRuntime().addShutdownHook(new Thread(HibernateUtil::shutdown));


        try (EntityManager em = HibernateUtil.createEntityManager()) {
            var tx = em.getTransaction();
            try {
                tx.begin();
                Player player1;
                Player player2;

                //Roger Federer vs Rafael Nadal, Rafael Nadal won, 2008-07-06
                //                player1 = em.find(Player.class, 1L);
                //                player2 = em.find(Player.class, 2L);
                //                var match = new Match(player1, player2, player2);
                //                match.setPlayedAt(LocalDate.of(2008, 7, 6));
                //                em.persist(match);

                //Roger Federer vs Pete Sampras, Roger Federer won, 2001-07-02
                player1 = em.find(Player.class, 1L);
                player2 = em.find(Player.class, 6L);
                if (!player1.getLastName().equals("Federer") || !player2.getLastName().equals("Sampras")) {
                    throw new IllegalStateException("Players should be Roger Federer and Pete Sampras");
                }
                var match2 = new Match(player1, player2, player1);
                match2.setPlayedAt(LocalDate.of(2001, 7, 2));
                em.persist(match2);

                //                Roger Federer,Andre Agassi,Roger Federer,2005-09-11
                player1 = em.find(Player.class, 1L);
                player2 = em.find(Player.class, 6L);
                if (!player1.getLastName().equals("Federer") || !player2.getLastName().equals("Agassi")) {
                    throw new IllegalStateException("Players should be Roger Federer and Andre Agassi");
                }
                var match3 = new Match(player1, player2, player1);
                match3.setPlayedAt(LocalDate.of(2001, 7, 2));
                em.persist(match3);

                //                Rafael Nadal,Andre Agassi,Rafael Nadal,2006-07-01
                player1 = em.find(Player.class, 2L);
                player2 = em.find(Player.class, 12L);
                if (!player1.getLastName().equals("Nadal") || !player2.getLastName().equals("Agassi")) {
                    throw new IllegalStateException("Players should be Rafael Nadal and Andre Agassi");
                }
                var match4 = new Match(player1, player2, player1);
                match4.setPlayedAt(LocalDate.of(2006, 7, 1));
                em.persist(match4);

                //                Rod Laver,Bjorn Borg,Bjorn Borg,1976-10-11
                player1 = em.find(Player.class, 3L);
                player2 = em.find(Player.class, 7L);
                if (!player1.getLastName().equals("Laver") || !player2.getLastName().equals("Borg")) {
                    throw new IllegalStateException("Players should be Rod Laver and Bjorn Borg");
                }
                var match5 = new Match(player1, player2, player2);
                match5.setPlayedAt(LocalDate.of(1976, 10, 11));
                em.persist(match5);

                //                Pete Sampras,Andre Agassi,Pete Sampras,2002-09-08
                player1 = em.find(Player.class, 6L);
                player2 = em.find(Player.class, 12L);
                if (!player1.getLastName().equals("Sampras") || !player2.getLastName().equals("Agassi")) {
                    throw new IllegalStateException("Players should be Pete Sampras and Andre Agassi");
                }
                var match6 = new Match(player1, player2, player1);
                match6.setPlayedAt(LocalDate.of(2002, 9, 8));
                em.persist(match6);

                //Steffi Graf,Martina Navratilova,Steffi Graf,1988-07-02
                player1 = em.find(Player.class, 4L);
                player2 = em.find(Player.class, 5L);
                if (!player1.getLastName().equals("Graf") || !player2.getLastName().equals("Navratilova")) {
                    throw new IllegalStateException("Players should be Steffi Graf and Martina Navratilova");
                }
                var match7 = new Match(player1, player2, player1);
                match7.setPlayedAt(LocalDate.of(1988, 7, 2));
                em.persist(match7);

                //Steffi Graf,Chris Evert,Steffi Graf,1988-01-24
                player1 = em.find(Player.class, 4L);
                player2 = em.find(Player.class, 9L);
                if (!player1.getLastName().equals("Graf") || !player2.getLastName().equals("Evert")) {
                    throw new IllegalStateException("Players should be Steffi Graf and Chris Evert");
                }
                var match8 = new Match(player1, player2, player1);
                match8.setPlayedAt(LocalDate.of(1988, 1, 24));
                em.persist(match8);

                //Martina Navratilova,Chris Evert,Chris Evert,1985-06-08
                player1 = em.find(Player.class, 5L);
                player2 = em.find(Player.class, 9L);
                if (!player1.getLastName().equals("Navratilova") || !player2.getLastName().equals("Evert")) {
                    throw new IllegalStateException("Players should be Martina Navratilova and Chris Evert");
                }
                var match9 = new Match(player1, player2, player2);
                match9.setPlayedAt(LocalDate.of(1985, 6, 8));
                em.persist(match9);

                //Margaret Court,Billie Jean King,Margaret Court,1963-07-08
                player1 = em.find(Player.class, 8L);
                player2 = em.find(Player.class, 10L);
                if (!player1.getLastName().equals("Court") || !player2.getLastName().equals("King")) {
                    throw new IllegalStateException("Players should be Margaret Court and Billie Jean King");
                }
                var match10 = new Match(player1, player2, player1);
                match10.setPlayedAt(LocalDate.of(1963, 7, 8));
                em.persist(match10);

                //Margaret Court,Chris Evert,Chris Evert,1973-07-05
                player1 = em.find(Player.class, 8L);
                player2 = em.find(Player.class, 9L);
                if (!player1.getLastName().equals("Court") || !player2.getLastName().equals("Evert")) {
                    throw new IllegalStateException("Players should be Margaret Court and Chris Evert");
                }
                var match11 = new Match(player1, player2, player2);
                match11.setPlayedAt(LocalDate.of(1973, 7, 5));
                em.persist(match11);

                //Margaret Court,Martina Navratilova,Martina Navratilova,1977-02-14
                player1 = em.find(Player.class, 8L);
                player2 = em.find(Player.class, 5L);
                if (!player1.getLastName().equals("Court") || !player2.getLastName().equals("Navratilova")) {
                    throw new IllegalStateException("Players should be Margaret Court and Martina Navratilova");
                }
                var match12 = new Match(player1, player2, player2);
                match12.setPlayedAt(LocalDate.of(1977, 2, 14));
                em.persist(match12);

                //Billie Jean King,Chris Evert,Billie Jean King,1973-07-07
                player1 = em.find(Player.class, 10L);
                player2 = em.find(Player.class, 9L);
                if (!player1.getLastName().equals("King") || !player2.getLastName().equals("Evert")) {
                    throw new IllegalStateException("Players should be Billie Jean King and Chris Evert");
                }
                var match13 = new Match(player1, player2, player1);
                match13.setPlayedAt(LocalDate.of(1973, 7, 7));
                em.persist(match13);

                //Billie Jean King,Martina Navratilova,Martina Navratilova,1980-06-30
                player1 = em.find(Player.class, 10L);
                player2 = em.find(Player.class, 5L);
                if (!player1.getLastName().equals("King") || !player2.getLastName().equals("Navratilova")) {
                    throw new IllegalStateException("Players should be Billie Jean King and Martina Navratilova");
                }
                var match14 = new Match(player1, player2, player2);
                match14.setPlayedAt(LocalDate.of(1980, 6, 30));
                em.persist(match14);


                tx.commit();

            } catch (Exception e) {
                if (tx.isActive()) {
                    tx.rollback();
                }
                log.error("Transaction failed in MatchesAdd.main()", e);
                throw e;
            }
        } catch (Exception e) {
            log.error("Unexpected error while running MatchesAdd", e);
        }


    }
}