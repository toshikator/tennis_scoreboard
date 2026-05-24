package pro.bukhman.service;

import jakarta.persistence.EntityManager;
import pro.bukhman.matchStorage.OngoingMatchStorage;
import pro.bukhman.model.OngoingMatch;
import pro.bukhman.model.entity.Player;

import java.util.UUID;

public class OngoingMatchesService extends BasicService {

    private final PlayerService playerService = new PlayerService(em);
    private final OngoingMatchStorage ongoingMatchStorage;

    public OngoingMatchesService(EntityManager em, OngoingMatchStorage ongoingMatchStorage) {
        super(em);
        this.ongoingMatchStorage = ongoingMatchStorage;
    }

    public UUID createMatch(Long player1Id, Long player2Id) {
        Player player1 = playerService.getPlayerById(player1Id);
        Player player2 = playerService.getPlayerById(player2Id);
        OngoingMatch match = new OngoingMatch(player1, player2);
        return ongoingMatchStorage.add(match);
    }
}
