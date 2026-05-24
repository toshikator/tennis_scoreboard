package pro.bukhman.service;

import jakarta.persistence.EntityManager;

public class MatchScoreCalculationService extends BasicService {

    public MatchScoreCalculationService(EntityManager em) {
        super(em);
    }
}
