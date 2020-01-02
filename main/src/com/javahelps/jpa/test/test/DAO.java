package com.javahelps.jpa.test.test;

import javax.persistence.EntityManager;

public class DAO {

    private EntityManager entityManager;

    public DAO(EntityManager entityManager) {
        this.entityManager = entityManager;
    }
}
