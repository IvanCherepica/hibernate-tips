package com.javahelps.jpa.test.n_plus_1_problem.mkyong_ex;

import com.javahelps.jpa.test.util.PersistentHelper;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class Bidirectional {
    public static void main(String[] args) {
        EntityManager entityManager = PersistentHelper.getEntityManager(new Class[]{Stock.class, StockDailyRecord.class});

        saveData(entityManager);
        entityManager.clear();

        entityManager.getTransaction().begin();

        //call select from stock
        Stock stock = entityManager.find(Stock.class, 1L);

        entityManager.getTransaction().commit();

        Set<StockDailyRecord> stockDailyRecords = stock.getStockDailyRecords();

        //call select from stock_daily_record
        for (StockDailyRecord stockDailyRecord : stockDailyRecords) {
            System.out.println(stockDailyRecord.getId());
        }
    }

    private static void saveData(EntityManager entityManager) {
        entityManager.getTransaction().begin();

        StockDailyRecord stockDailyRecord1 = new StockDailyRecord();
        StockDailyRecord stockDailyRecord2 = new StockDailyRecord();
        StockDailyRecord stockDailyRecord3 = new StockDailyRecord();

        entityManager.persist(stockDailyRecord1);
        entityManager.persist(stockDailyRecord2);
        entityManager.persist(stockDailyRecord3);

        Stock stock = new Stock();

        entityManager.persist(stock);

        stockDailyRecord1.setStock(stock);
        stockDailyRecord2.setStock(stock);
        stockDailyRecord3.setStock(stock);

        Set<StockDailyRecord> stockDailyRecords = new HashSet<>();
        stockDailyRecords.add(stockDailyRecord1);
        stockDailyRecords.add(stockDailyRecord2);
        stockDailyRecords.add(stockDailyRecord3);

        stock.setStockDailyRecords(stockDailyRecords);

        entityManager.getTransaction().commit();
    }


    @Entity
    @Table(name = "stock_daily_record")
    public static class StockDailyRecord {
        @Id
        @GeneratedValue
        private long id;

        @ManyToOne(fetch =  FetchType.LAZY)
        @JoinColumn(name = "stock_id")
        private Stock stock;

        public StockDailyRecord() {
        }

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public Stock getStock() {
            return stock;
        }

        public void setStock(Stock stock) {
            this.stock = stock;
        }
    }

    @Entity
    @Table(name = "stock")
    public static class Stock implements Serializable {

        @Id
        @GeneratedValue
        private long id;

        @OneToMany(fetch = FetchType.LAZY, mappedBy = "stock", cascade = CascadeType.ALL)
	    @Fetch(FetchMode.SELECT)
	    @BatchSize(size = 10)
        private Set<StockDailyRecord> stockDailyRecords = new HashSet<>();

        public Stock() {
        }

        public void setId(long id) {
            this.id = id;
        }

        public Set<StockDailyRecord> getStockDailyRecords() {
            return this.stockDailyRecords;
        }

        public void setStockDailyRecords(Set<StockDailyRecord> stockDailyRecords) {
            this.stockDailyRecords = stockDailyRecords;
        }
    }
}

