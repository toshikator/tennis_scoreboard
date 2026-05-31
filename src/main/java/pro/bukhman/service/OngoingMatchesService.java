package pro.bukhman.service;

import jakarta.persistence.EntityManager;
import pro.bukhman.exception.MatchIsAlreadyFinishedException;
import pro.bukhman.exception.MatchNotFoundException;
import pro.bukhman.exception.ResourceNotFoundException;
import pro.bukhman.model.entity.Player;
import pro.bukhman.ongoingMatchStorage.OngoingMatchStorage;
import pro.bukhman.model.OngoingMatch;
import pro.bukhman.model.dto.PlayerDto;

import java.util.UUID;


public class OngoingMatchesService extends BasicService {

    private final PlayerService playerService;
    private final OngoingMatchStorage ongoingMatchStorage;
    private final MatchesService finishedMatchService;

    public OngoingMatchesService(EntityManager em, OngoingMatchStorage ongoingMatchStorage) {
        super(em);
        this.playerService = new PlayerService(em);
        this.ongoingMatchStorage = ongoingMatchStorage;
        this.finishedMatchService = new MatchesService(em);
    }

    public UUID createMatch(Long player1Id, Long player2Id) {
        PlayerDto player1Dto = playerService.getPlayerDtoById(player1Id);
        PlayerDto player2Dto = playerService.getPlayerDtoById(player2Id);
        OngoingMatch match = new OngoingMatch(player1Dto, player2Dto);
        return ongoingMatchStorage.add(match);
    }

    public void addPoint(UUID matchId, Long playerId) {
        OngoingMatch match = ongoingMatchStorage.getById(matchId)
                .orElseThrow(() -> new MatchNotFoundException("Match not found"));
        try {
            PlayerDto player = match.getPlayerById(playerId);
        } catch (Exception e) {
            throw new ResourceNotFoundException("Player not found", e.getCause());
        }

        if (match.isFinished()) {
            throw new MatchIsAlreadyFinishedException("Match is already finished. Cannot add points to it.");
        }

        PlayerDto pointWinner = match.getPlayerById(playerId);
        match.addPoint(pointWinner);

        if (match.isFinished()) {
            Player player1 = playerService.getPlayerById(match.getPlayer1().id());
            Player player2 = playerService.getPlayerById(match.getPlayer2().id());
            Player winner = playerService.getPlayerById(match.getWinner().id());
            finishedMatchService.createMatch(player1, player2, winner);
        }
    }


}
