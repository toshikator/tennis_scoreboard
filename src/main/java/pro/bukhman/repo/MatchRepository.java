package pro.bukhman.repo;

import jakarta.persistence.EntityManager;
import pro.bukhman.model.entity.Match;

import java.util.List;

public class MatchRepository extends BasicRepository<Match, Long> {
    public MatchRepository(EntityManager em, Class<Match> entityClass) {
        super(em, entityClass);
    }

    public List<Match> findMatchesByPlayerFirstname(String playerFirstname) {
        return em.createQuery("""
                        SELECT m
                        FROM Match m
                        WHERE m.player1.firstName = :playerName
                           OR m.player2.firstName = :playerName
                        """, Match.class)
                .setParameter("playerName", playerFirstname)
                .getResultList();
    }

    public List<Match> findMatchesByPlayerLastname(String playerLastname) {
        return em.createQuery("""
                        SELECT m
                        FROM Match m
                        WHERE m.player1.lastName = :playerName
                           OR m.player2.lastName = :playerName
                        """, Match.class)
                .setParameter("playerName", playerLastname)
                .getResultList();
    }

    public List<Match> findMatchesByPlayerFullName(String playerLastname, String playerFirstname) {
        return em.createQuery("""
                SELECT m FROM Match m
                WHERE m.player1.firstName=:playerFirstname AND 
                m.player1.lastName=:playerLastname
                OR m.player2.firstName=:playerFirstname AND 
                m.player2.lastName=:playerLastname
                """, Match.class).setParameter("playerLastname", playerLastname).setParameter("playerFirstname", playerFirstname).getResultList();
    }
}
