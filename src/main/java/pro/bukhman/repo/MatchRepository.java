package pro.bukhman.repo;

import jakarta.persistence.EntityManager;
import pro.bukhman.model.entity.FinishedMatch;

public class MatchRepository extends BasicRepository<FinishedMatch, Long> {
    public MatchRepository(EntityManager em, Class<FinishedMatch> entityClass) {
        super(em, entityClass);
    }
}
