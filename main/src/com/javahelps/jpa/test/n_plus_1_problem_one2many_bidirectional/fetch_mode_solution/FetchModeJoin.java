package com.javahelps.jpa.test.n_plus_1_problem_one2many_bidirectional.fetch_mode_solution;

import com.javahelps.jpa.test.util.PersistentHelper;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FetchModeJoin {
    public static void main(String[] args) {
        EntityManager entityManager = PersistentHelper.getEntityManager(new Class[]{Stock.class, StockDailyRecord.class});

        saveData(entityManager);
        entityManager.clear();

        {//использование FetchMode.JOIN равносильно использованию FetchType.EAGER
            //данные будут подругжаться принудительно, даже если в них нет необходимости
            entityManager.getTransaction().begin();

            System.out.println();
            System.out.println("Before one stock select without collection call");
            System.out.println();

            Stock stock = entityManager.find(Stock.class, 1L);

            System.out.println();
            System.out.println("After one stock select without collection call");
            System.out.println();

            entityManager.getTransaction().commit();
        }

        entityManager.clear();

        {//если мы достаём запись из базы, используя метод entitymanager, то срабатывает загрузка и данные достаются одним запросом; без n+1
            entityManager.getTransaction().begin();

            System.out.println();
            System.out.println("Before one stock select with collection call");
            System.out.println();

            Stock stock = entityManager.find(Stock.class, 1L);

            System.out.println();
            System.out.println("After one stock select with collection call");
            System.out.println();

            for (StockDailyRecord stockDailyRecord : stock.getStockDailyRecords()) {
                System.out.println(stockDailyRecord.getId());
            }

            entityManager.getTransaction().commit();
        }

        entityManager.clear();

        {//та же самая история с FetchJoin - n+1 запросы и необходимость использовать join fetch в hql
            entityManager.getTransaction().begin();

            System.out.println();
            System.out.println("Before list select");
            System.out.println();

            List<Stock> list = entityManager.createQuery("from " + Stock.class.getName(), Stock.class).getResultList();

            System.out.println();
            System.out.println("After list select");
            System.out.println();

            for (Stock stock : list) {

                for (StockDailyRecord stockDailyRecord : stock.getStockDailyRecords()) {
                    System.out.println(stockDailyRecord.getId());
                }

            }

            entityManager.getTransaction().commit();
        }
    }

    private static void saveData(EntityManager entityManager) {
        entityManager.getTransaction().begin();

        StockDailyRecord stockDailyRecord1 = new StockDailyRecord();
        StockDailyRecord stockDailyRecord2 = new StockDailyRecord();
        StockDailyRecord stockDailyRecord3 = new StockDailyRecord();

        StockDailyRecord stockDailyRecord4 = new StockDailyRecord();
        StockDailyRecord stockDailyRecord5 = new StockDailyRecord();
        StockDailyRecord stockDailyRecord6 = new StockDailyRecord();

        StockDailyRecord stockDailyRecord7 = new StockDailyRecord();
        StockDailyRecord stockDailyRecord8 = new StockDailyRecord();
        StockDailyRecord stockDailyRecord9 = new StockDailyRecord();

        entityManager.persist(stockDailyRecord1);
        entityManager.persist(stockDailyRecord2);
        entityManager.persist(stockDailyRecord3);

        entityManager.persist(stockDailyRecord4);
        entityManager.persist(stockDailyRecord5);
        entityManager.persist(stockDailyRecord6);

        entityManager.persist(stockDailyRecord7);
        entityManager.persist(stockDailyRecord8);
        entityManager.persist(stockDailyRecord9);

        Stock stock1 = new Stock();
        Stock stock2 = new Stock();
        Stock stock3 = new Stock();

        entityManager.persist(stock1);
        entityManager.persist(stock2);
        entityManager.persist(stock3);

        stock1.addStockDailyRecord(stockDailyRecord1);
        stock1.addStockDailyRecord(stockDailyRecord2);
        stock1.addStockDailyRecord(stockDailyRecord3);

        stock2.addStockDailyRecord(stockDailyRecord4);
        stock2.addStockDailyRecord(stockDailyRecord5);
        stock2.addStockDailyRecord(stockDailyRecord6);

        stock3.addStockDailyRecord(stockDailyRecord7);
        stock3.addStockDailyRecord(stockDailyRecord8);
        stock3.addStockDailyRecord(stockDailyRecord9);

        entityManager.getTransaction().commit();
    }


    @Entity
    @Table(name = "stock_daily_record")
    private static class StockDailyRecord {
        @Id
        @GeneratedValue
        private long id;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "stock_id")
        private Stock stock;

        public StockDailyRecord() {
        }

        public Stock getStock() {
            return stock;
        }

        public void setStock(Stock stock) {
            this.stock = stock;
        }

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        @Override
        public String toString() {
            return "StockDailyRecord{" +
                    "id=" + id +
                    '}';
        }
    }

    @Entity
    @Table(name = "stock")
    private static class Stock implements Serializable {

        @Id
        @GeneratedValue
        private long id;

        @Fetch(FetchMode.JOIN)
        @OneToMany(mappedBy = "stock", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
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

        public void addStockDailyRecord(StockDailyRecord stockDailyRecord) {
            this.stockDailyRecords.add(stockDailyRecord);
            stockDailyRecord.setStock(this);
        }

        public void removeStockDailyRecord(StockDailyRecord stockDailyRecord) {
            this.stockDailyRecords.remove(stockDailyRecord);
            stockDailyRecord.setStock(null);
        }

        @Override
        public String toString() {
            return "Stock{" +
                    "id=" + id +
                    '}';
        }
    }
}
