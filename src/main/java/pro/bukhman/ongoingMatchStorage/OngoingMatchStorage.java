package pro.bukhman.ongoingMatchStorage;

import pro.bukhman.exception.TooManyActiveMatchesException;
import pro.bukhman.model.OngoingMatch;
import pro.bukhman.model.dto.OngoingMatchDto;
import pro.bukhman.model.dto.PlayerDto;
import pro.bukhman.model.dto.OngoingMatchSnapshot;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class OngoingMatchStorage {
    private static final long MATCH_TTL_MINUTES = 3 * 24 * 60;
    private static final int MAX_ACTIVE_MATCHES = 100;
    private final ConcurrentHashMap<UUID, OngoingMatch> ongoingMatches = new ConcurrentHashMap<>();

    public UUID add(OngoingMatch match) {
        removeOldMatches();
        if (ongoingMatches.size() >= MAX_ACTIVE_MATCHES) {
            throw new TooManyActiveMatchesException("Too many active matches");
        }
        UUID id = UUID.randomUUID();
        ongoingMatches.put(id, match);
        return id;
    }

    private void removeOldMatches() {
        ongoingMatches.entrySet().removeIf(entry -> (Math.abs(Duration.between(entry.getValue().getStartedAt(), LocalDateTime.now()).toMinutes())) > MATCH_TTL_MINUTES);
    }

    public Optional<OngoingMatch> getById(UUID id) {
        return Optional.ofNullable(ongoingMatches.get(id));
    }

    public OngoingMatchDto getDtoByUUID(UUID id) {
        OngoingMatch ongoingMatch = getById(id).orElseThrow(() -> new IllegalArgumentException("Match not found"));
        OngoingMatchSnapshot s = ongoingMatch.getSnapshot();
        PlayerDto p1 = s.getPlayer1();
        PlayerDto p2 = s.getPlayer2();
        OngoingMatchDto dto = new OngoingMatchDto(id, p1.id(), p2.id(), p1.firstName(), p2.firstName(), p1.lastName(),
                p2.lastName(), s.getPlayer1Points(), s.getPlayer2Points(), s.getPlayer1Sets(), s.getPlayer2Sets(),
                s.getPlayer1Games(), s.getPlayer2Games(), ongoingMatch.isFinished());
        return dto;
    }

    public void remove(UUID id) {
        ongoingMatches.remove(id);
    }

    public boolean exists(UUID id) {
        return ongoingMatches.containsKey(id);
    }
}
