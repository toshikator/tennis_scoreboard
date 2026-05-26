package pro.bukhman.service;

import jakarta.persistence.EntityManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import pro.bukhman.exception.ResourceAlreadyExistsException;
import pro.bukhman.exception.ResourceNotFoundException;
import pro.bukhman.model.dto.PaginationDto;
import pro.bukhman.model.dto.PlayerDto;
import pro.bukhman.model.dto.ResponsePaginationDto;
import pro.bukhman.model.entity.Player;
import pro.bukhman.repo.PlayerRepository;

import java.util.List;

public class PlayerService extends BasicService {
    private static final Logger logger = LogManager.getLogger(PlayerService.class);

    private final PlayerRepository playerRepository;

    public PlayerService(EntityManager em) {
        super(em);
        this.playerRepository = new PlayerRepository(this.em, Player.class);
    }

    public ResponsePaginationDto<PlayerDto> getPlayersPagination(Integer limit, Integer offset) {
        Long count = playerRepository.countAll();
        if (limit == null || offset == null) {
            limit = 1;
            offset = 0;
        }
        if (limit > 50) {
            limit = 50;
        }
        if (offset < 0) {
            offset = 0;
        }
        List<Player> players = playerRepository.findPage(offset, limit);
        List<PlayerDto> playersDto = players.stream().map(player -> new PlayerDto(player.getId(), player.getFirstName(), player.getLastName())).toList();
        PaginationDto paginationDto = new PaginationDto(offset, limit, count);
        return new ResponsePaginationDto<PlayerDto>(playersDto, paginationDto);
    }

    public Player getPlayerById(Long id) {
        Player player = playerRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Player not found"));
        return player;
    }

    public PlayerDto getPlayerDtoById(Long id) {
        Player player = playerRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Player not found"));
        return new PlayerDto(player.getId(), player.getFirstName(), player.getLastName());
    }

    public List<PlayerDto> getAllPlayersDto() {
        List<Player> players = playerRepository.findAll();
        return players.stream().map(player -> new PlayerDto(player.getId(), player.getFirstName(), player.getLastName())).toList();
    }

    public List<PlayerDto> getPlayersByFirstname(String firstName) {
        List<Player> players = playerRepository.findByFirstName(firstName);
        return players.stream().map(player -> new PlayerDto(player.getId(), player.getFirstName(), player.getLastName())).toList();
    }

    public List<PlayerDto> getPlayersByLastname(String lastName) {
        List<Player> players = playerRepository.findByLastName(lastName);
        return players.stream().map(player -> new PlayerDto(player.getId(), player.getFirstName(), player.getLastName())).toList();
    }

    public Long getPlayerCount() {
        return playerRepository.countAll();
    }

    public Player createPlayer(String firstName, String lastName) {
        try {
            em.getTransaction().begin();
            Player player1 = playerRepository.save(new Player(firstName, lastName));
            em.getTransaction().commit();
            return player1;

        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            logger.error("Failed to create player: firstName='{}', lastName='{}'", firstName, lastName, e);
            throw new ResourceAlreadyExistsException("Player with this first name and last name already exists", e.getCause());
        }
    }
}
