package pro.bukhman.service;

import jakarta.persistence.EntityManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pro.bukhman.exception.ResourceNotFoundException;
import pro.bukhman.model.entity.Player;
import pro.bukhman.repo.PlayerRepository;

public class PlayerService extends BasicService {
    private static final Logger logger = LogManager.getLogger(PlayerService.class);

    private final PlayerRepository playerRepository;

    public PlayerService(EntityManager em) {
        super(em);
        this.playerRepository = new PlayerRepository(this.em, Player.class);
    }

    public Player getPlayerById(Long id) {
        return playerRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Player not found"));
    }

    public Player createPlayer(String firstName, String lastName) {
        try {
            em.getTransaction().begin();
            PlayerRepository playerRepository = new PlayerRepository(em, Player.class);
            Player player1 = playerRepository.save(new Player(firstName, lastName));
            em.getTransaction().commit();
            return player1;
        } catch (RuntimeException e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            logger.error("Failed to create player: firstName='{}', lastName='{}'", firstName, lastName, e);
            throw e;
        }
    }
}
