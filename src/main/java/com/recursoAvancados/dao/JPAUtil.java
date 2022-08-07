package com.recursoAvancados.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class JPAUtil {
    private static EntityManagerFactory factory;
    static {
        factory = Persistence.createEntityManagerFactory("recursos_avancados");
    }
    public static EntityManager getEntityManager() {
        return factory.createEntityManager();
    }
    public static void close() {
        factory.close();
    }

}
