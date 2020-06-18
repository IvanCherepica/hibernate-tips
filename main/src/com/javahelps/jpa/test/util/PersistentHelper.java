package com.javahelps.jpa.test.util;

import com.javahelps.jpa.test.PersistenceUnitInfoImpl;
import com.javahelps.jpa.test.model.Post;
import com.javahelps.jpa.test.model.PostComment;
import org.hibernate.jpa.HibernatePersistenceProvider;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.hibernate.cfg.AvailableSettings.*;

public class PersistentHelper {

    private static EntityManager entityManager;
    private static Class[] pcs;
    private static EntityManagerFactory entityManagerFactory;


    public static EntityManager getEntityManager(Class[] persistentClasses) {
        if (entityManager != null) {
            if (Arrays.equals(persistentClasses, pcs)) {
                return entityManager;
            }
        }

        if (entityManagerFactory != null) {
            entityManager = entityManagerFactory.createEntityManager();
            return entityManager;
        }

        PersistenceUnitInfoImpl persistenceUnitInfo = new PersistenceUnitInfoImpl(persistentClasses);

        entityManagerFactory = new HibernatePersistenceProvider().createContainerEntityManagerFactory(persistenceUnitInfo, getPostgreSQLOption());
        entityManager = entityManagerFactory.createEntityManager();

        return entityManager;
    }

    private static Map<String, Object> getMysqlInnoDbOption() {
        Map<String, Object> options = new HashMap<>();
        options.put(DRIVER, "com.mysql.jdbc.Driver");
        options.put(URL, "jdbc:mysql://localhost:3306/hibernate_examples?characterEncoding=UTF-8&useUnicode=true&useSSL=false&serverTimezone=UTC");
//        options.put(DIALECT, "org.hibernate.dialect.MySQL5Dialect");
        options.put(DIALECT, "org.hibernate.dialect.MySQL5InnoDBDialect");
        options.put(USER, "root");
        options.put(PASS, "root");
        options.put(HBM2DDL_AUTO, "create-drop");
        options.put(SHOW_SQL, true);

        return options;
    }

    private static Map<String, Object> getPostgreSQLOption() {
        Map<String, Object> options = new HashMap<>();
        options.put(DRIVER, "org.postgresql.Driver");
        options.put(URL, "jdbc:postgresql://localhost:5432/postgres_demo");
        options.put(DIALECT, "org.hibernate.dialect.PostgreSQL95Dialect");
        options.put(USER, "postgres");
        options.put(PASS, "postgres");
        options.put(HBM2DDL_AUTO, "none");
        options.put(SHOW_SQL, true);

        return options;
    }
}
