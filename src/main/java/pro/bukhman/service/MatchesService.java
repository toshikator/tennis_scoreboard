package pro.bukhman.service;

import jakarta.persistence.EntityManager;
import org.apache.logging.log4j.LogManager;

import org.apache.logging.log4j.Logger;
import pro.bukhman.exception.ResourceAlreadyExistsException;
import pro.bukhman.exception.ResourceNotFoundException;
import pro.bukhman.model.dto.MatchDto;
import pro.bukhman.model.dto.PaginationDto;
import pro.bukhman.model.dto.PlayerDto;
import pro.bukhman.model.dto.ResponsePaginationDto;
import pro.bukhman.model.entity.Match;
import pro.bukhman.model.entity.Player;
import pro.bukhman.repo.MatchRepository;

import java.util.List;


public class MatchesService extends BasicService {
    private final MatchRepository matchRepository;
    private final Logger logger = LogManager.getLogger(MatchesService.class);

    public MatchesService(EntityManager em) {
        super(em);
        matchRepository = new MatchRepository(em, Match.class);
    }

    public ResponsePaginationDto<MatchDto> getMatchesPagination(Integer limit, Integer offset) {
        Long count = matchRepository.countAll();
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
        List<Match> matches = matchRepository.findPage(offset, limit);
        List<MatchDto> matchesDto = matches.stream().map(match -> new MatchDto(match.getPlayer1().getId(), match.getPlayer2().getId(), match.getWinner().getId(), match.getId())).toList();
        PaginationDto paginationDto = new PaginationDto(offset, limit, count);
        return new ResponsePaginationDto<MatchDto>(matchesDto, paginationDto);
    }

    public MatchDto getMatchDtoById(Long id) {
        Match match = matchRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Match not found"));
        return new MatchDto(match.getPlayer1().getId(), match.getPlayer2().getId(), match.getWinner().getId(), match.getId());
    }

    public Match getMatchById(Long id) {
        return matchRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Match not found"));
    }

    public List<Match> getAllMatches() {
        return matchRepository.findAll();
    }

    public List<MatchDto> getAllMatchesDto() {
        List<Match> matches = matchRepository.findAll();
        return matches.stream().map(match -> new MatchDto(match.getPlayer1().getId(), match.getPlayer2().getId(), match.getWinner().getId(), match.getId())).toList();
    }

    public List<Match> getMatchesByPlayerFirstname(String playerFirstname) {
        return matchRepository.findMatchesByPlayerFirstname(playerFirstname);
    }

    public List<MatchDto> getMatchesByPlayerFirstnameDto(String playerFirstname) {
        List<Match> matches = matchRepository.findMatchesByPlayerFirstname(playerFirstname);
        return matches.stream().map(match -> new MatchDto(match.getPlayer1().getId(), match.getPlayer2().getId(), match.getWinner().getId(), match.getId())).toList();
    }

    public List<MatchDto> getMatchesByPlayerLastnameDto(String playerLastname) {
        List<Match> matches = matchRepository.findMatchesByPlayerLastname(playerLastname);
        return matches.stream().map(match -> new MatchDto(match.getPlayer1().getId(), match.getPlayer2().getId(), match.getWinner().getId(), match.getId())).toList();
    }

    public List<Match> getMatchesByPlayerLastname(String playerLastname) {
        return matchRepository.findMatchesByPlayerLastname(playerLastname);
    }

    public Long getMatchCount() {
        return matchRepository.countAll();
    }

    public Match createMatch(Player player1, Player player2, Player winner) {
        try {
            em.getTransaction().begin();
            Match match = matchRepository.save(new Match(player1, player2, winner));
            em.getTransaction().commit();
            return match;

        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            logger.error("Failed to create match: player1='{}', player2='{}', winner='{}'", player1.getFirstName(), player2.getFirstName(), winner.getFirstName());
            throw new ResourceAlreadyExistsException("Match with this players already exists", e.getCause());

        }
    }
}
