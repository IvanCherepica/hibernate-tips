package com.javahelps.jpa.test.util;

import com.javahelps.jpa.test.PersistenceUnitInfoImpl;
import com.javahelps.jpa.test.model.Post;
import com.javahelps.jpa.test.model.PostComment;
import org.hibernate.Interceptor;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.jpa.HibernatePersistenceProvider;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.hibernate.cfg.AvailableSettings.*;

public class PersistentHelper {

    private static final DB USED_DB = DB.POSTGRESQL;

    private static final Map<String, Object> options = new HashMap<>();
    private static EntityManager entityManager;
    private static Class[] pcs;
    private static EntityManagerFactory entityManagerFactory;


    public static EntityManager getEntityManager(Class[] persistentClasses) {

        switch (USED_DB) {
            case MYSQL:
                initMysqlInnoDbOption();
                break;
            case POSTGRESQL:
                initPostgreSQLOption();
        }

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

        entityManagerFactory = new HibernatePersistenceProvider().createContainerEntityManagerFactory(persistenceUnitInfo, options);
        entityManager = entityManagerFactory.createEntityManager();

        return entityManager;
    }

    private static void initMysqlInnoDbOption() {
        options.put(DRIVER, "com.mysql.jdbc.Driver");
        options.put(URL, "jdbc:mysql://localhost:3306/hibernate_examples?characterEncoding=UTF-8&useUnicode=true&useSSL=false&serverTimezone=UTC");
//        options.put(DIALECT, "org.hibernate.dialect.MySQL5Dialect");
        options.put(DIALECT, "org.hibernate.dialect.MySQL5InnoDBDialect");
        options.put(USER, "root");
        options.put(PASS, "root");
        options.put(HBM2DDL_AUTO, "create-drop");
        options.put(SHOW_SQL, true);
    }

    private static void initPostgreSQLOption() {
//        org.postgresql.Driver
        options.put(DRIVER, "org.postgresql.Driver");
        options.put(URL, "jdbc:postgresql://localhost:5432/postgres_demo?reWriteBatchedInserts=true");
        options.put(DIALECT, "org.hibernate.dialect.PostgreSQL95Dialect");
        options.put(USER, "postgres");
        options.put(PASS, "postgres");
        options.put(HBM2DDL_AUTO, "create");
        options.put(SHOW_SQL, true);

        options.put(STATEMENT_BATCH_SIZE, "20");
        options.put(ORDER_INSERTS, "true");
        options.put(ORDER_UPDATES, "true");
        options.put(BATCH_VERSIONED_DATA, "true");
        options.put(GENERATE_STATISTICS, "true");
    }

    private enum DB {
        MYSQL,
        POSTGRESQL
    }

    public static void setIsolationLevel(IsolationLevel isolationLevel) {
        options.put("hibernate.connection.isolation", isolationLevel.getValue());
    }

    public static void setInterceptor(Interceptor interceptor) {
        options.put(AvailableSettings.INTERCEPTOR, interceptor);
    }
}
