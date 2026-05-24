package pro.bukhman.service;

import jakarta.persistence.EntityManager;

public class FinishedMatchesPersistenceService extends BasicService {

    public FinishedMatchesPersistenceService(EntityManager em) {
        super(em);
    }
}
