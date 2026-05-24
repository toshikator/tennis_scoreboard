package pro.bukhman.repo;

import jakarta.persistence.EntityManager;
import pro.bukhman.model.entity.Player;

public class PlayerRepository extends BasicRepository<Player, Long> {
    public PlayerRepository(EntityManager em, Class<Player> entityClass) {
        super(em, entityClass);
    }
}
