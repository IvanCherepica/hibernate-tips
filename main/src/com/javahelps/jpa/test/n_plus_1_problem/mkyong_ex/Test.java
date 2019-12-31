package com.javahelps.jpa.test.n_plus_1_problem.mkyong_ex;

import com.javahelps.jpa.test.util.PersistentHelper;

import javax.persistence.EntityManager;

public class Test {
    public static void main(String[] args) {
        EntityManager entityManager = PersistentHelper.getEntityManager(new Class[]{Stock.class, StockDailyRecord.class});
    }
}
