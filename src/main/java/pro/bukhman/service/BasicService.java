package pro.bukhman.service;

import jakarta.persistence.EntityManager;

public abstract class BasicService {
    protected final EntityManager em;

    protected BasicService(EntityManager em) {
        this.em = em;
    }
}
