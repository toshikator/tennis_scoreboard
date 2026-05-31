package pro.bukhman.repo;

import jakarta.persistence.EntityManager;
import pro.bukhman.model.entity.Player;

import java.util.List;
import java.util.Optional;

public class PlayerRepository extends BasicRepository<Player, Long> {
    public PlayerRepository(EntityManager em, Class<Player> entityClass) {
        super(em, entityClass);
    }

    public List<Player> findByFirstName(String firstName) {

        return em.createQuery(
                        "from Player p where p.firstName = :firstName",
                        Player.class
                )
                .setParameter("firstName", firstName)
                .getResultList();

    }

    public List<Player> findByLastName(String lastName) {

        return em.createQuery(
                        "from Player p where p.lastName = :lastName",
                        Player.class
                )
                .setParameter("lastName", lastName)
                .getResultList();

    }

    public Optional<Player> findByFullname(String firstName, String lastName) {
        List<Player> players = em.createQuery("""
                        select p
                        from Player p
                        where p.firstName = :firstName
                          and p.lastName = :lastName
                        """, Player.class)
                .setParameter("firstName", firstName)
                .setParameter("lastName", lastName)
                .setMaxResults(1)
                .getResultList();

        return players.stream().findFirst();
    }

}
