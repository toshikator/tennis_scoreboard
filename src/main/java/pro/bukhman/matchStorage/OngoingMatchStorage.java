package pro.bukhman.matchStorage;

import pro.bukhman.exception.TooManyActiveMatchesException;
import pro.bukhman.model.OngoingMatch;

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

    public Optional<OngoingMatch> findById(UUID id) {
        return Optional.ofNullable(ongoingMatches.get(id));
    }

    public void remove(UUID id) {
        ongoingMatches.remove(id);
    }

    public boolean exists(UUID id) {
        return ongoingMatches.containsKey(id);
    }
}
