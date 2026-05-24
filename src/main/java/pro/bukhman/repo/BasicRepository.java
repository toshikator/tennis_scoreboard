package pro.bukhman.repo;

import jakarta.persistence.EntityManager;

import java.util.List;
import java.util.Optional;

public class BasicRepository<T, ID> {

    protected final EntityManager em;
    private final Class<T> entityClass;

    public BasicRepository(EntityManager em, Class<T> entityClass) {
        this.em = em;
        this.entityClass = entityClass;
    }

    public T save(T entity) {
        em.persist(entity);
        return entity;
    }

    public T update(T entity) {
        return em.merge(entity);
    }

    public List<T> findAll() {
        String jpql = "select e from " + entityClass.getSimpleName() + " e";

        return em.createQuery(jpql, entityClass)
                .getResultList();
    }

    public List<T> findPage(int page, int size) {
        String jpql = "select e from " + entityClass.getSimpleName() + " e";

        return em.createQuery(jpql, entityClass)
                .setFirstResult((page - 1) * size)
                .setMaxResults(size)
                .getResultList();
    }

    public long count() {
        String jpql = "select count(e) from " + entityClass.getSimpleName() + " e";
        return em.createQuery(jpql, Long.class)
                .getSingleResult();
    }

    public void delete(T entity) {
        em.remove(entity);
    }

    public void deleteById(ID id) {
        findById(id).ifPresent(em::remove);
    }

    public Optional<T> findById(ID id) {
        return Optional.ofNullable(em.find(entityClass, id));
    }
}