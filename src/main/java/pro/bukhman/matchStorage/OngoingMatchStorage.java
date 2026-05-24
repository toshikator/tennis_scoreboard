package pro.bukhman.matchStorage;

import pro.bukhman.model.OngoingMatch;

import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class OngoingMatchStorage {
    private final ConcurrentHashMap<UUID, OngoingMatch> ongoingMatches = new ConcurrentHashMap<>();

    public UUID add(OngoingMatch match) {
        UUID id = UUID.randomUUID();
        ongoingMatches.put(id, match);
        return id;
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
