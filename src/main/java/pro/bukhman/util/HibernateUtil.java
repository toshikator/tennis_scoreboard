package pro.bukhman.util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.Map;

public class HibernateUtil {
    private static volatile EntityManagerFactory emf;
    private static volatile Map<String, Object> externalConfig;

    private HibernateUtil() {
    }

    public static void setConfig(Map<String, Object> config) {
        externalConfig = config;
    }

    private static EntityManagerFactory build() {
        if (externalConfig == null || externalConfig.isEmpty()) {
            throw new IllegalStateException("Hibernate configuration was not provided. Ensure AppInitializer sets it before first access.");
        }
        return Persistence.createEntityManagerFactory("tennisScoreboardPersistenceUnit", externalConfig);
    }

    public static EntityManagerFactory getEntityManagerFactory() {
        EntityManagerFactory localRef = emf;
        if (localRef == null) {
            synchronized (HibernateUtil.class) {
                localRef = emf;
                if (localRef == null) {
                    emf = localRef = build();
                }
            }
        }
        return localRef;
    }

    public static EntityManager createEntityManager() {
        return getEntityManagerFactory().createEntityManager();
    }

    public static void shutdown() {
        EntityManagerFactory localRef = emf;
        if (localRef != null && localRef.isOpen()) {
            localRef.close();
        }
    }
}
