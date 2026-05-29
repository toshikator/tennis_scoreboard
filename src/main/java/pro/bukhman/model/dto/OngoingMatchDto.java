package pro.bukhman.model.dto;

import java.util.UUID;

public record OngoingMatchDto(UUID matchUUID, Long player1Id, Long player2Id, String player1Firstname,
                              String player2Firstname,
                              String player1Lastname, String player2Lastname, Integer player1Score,
                              Integer player2Score,
                              Integer player1Set, Integer player2Set, Integer player1Game, Integer player2Game) {
}
