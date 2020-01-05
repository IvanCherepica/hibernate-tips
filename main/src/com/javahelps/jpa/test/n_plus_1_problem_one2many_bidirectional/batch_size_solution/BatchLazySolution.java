package com.javahelps.jpa.test.n_plus_1_problem_one2many_bidirectional.batch_size_solution;

import com.javahelps.jpa.test.util.PersistentHelper;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class BatchLazySolution {
    public static void main(String[] args) {
        EntityManager entityManager = PersistentHelper.getEntityManager(new Class[]{Stock.class, StockDailyRecord.class});

        saveData(entityManager);
        entityManager.clear();

        {//при использовании ленивой загрузки мы лишаемся необходимости каждый раз загружать в память всю информацию
            entityManager.getTransaction().begin();

            System.out.println();
            System.out.println("Before one stock select without collection call");
            System.out.println();

            Stock stock = entityManager.find(Stock.class, 1L);
            StockDailyRecord stockDailyRecord = entityManager.find(StockDailyRecord.class, 1L);

            System.out.println();
            System.out.println("After one stock select without collection call");
            System.out.println();

            entityManager.getTransaction().commit();
        }

        entityManager.clear();

        {//используя FetchMode.SUBSELECT мы и на таком запросе приобретаем один дополнительный запрос (но не более_
            entityManager.getTransaction().begin();

            System.out.println();
            System.out.println("Before one stock select");
            System.out.println();

            Stock stock = entityManager.find(Stock.class, 1L);
            StockDailyRecord stockDailyRecord = entityManager.find(StockDailyRecord.class, 1L);

            System.out.println();
            System.out.println("After one stock select");
            System.out.println();

            for (StockDailyRecord stockDailyRecord1 : stock.getStockDailyRecords()) {
                System.out.println(stockDailyRecord1.getId());
            }

            System.out.println(stockDailyRecord.getStock());

            entityManager.getTransaction().commit();
        }

        entityManager.clear();

        {//однако, если запросов на загрузку коллекций у нас много - то subselect объеденит их в один. в итоге устранив n+1
            entityManager.getTransaction().begin();

            System.out.println();
            System.out.println("Before list select");
            System.out.println();

//            List<Stock> list = entityManager.createQuery("from " + Stock.class.getName(), Stock.class).getResultList();
            List<StockDailyRecord> stockDailyRecords = entityManager.createQuery(
                    "FROM "+StockDailyRecord.class.getName(), StockDailyRecord.class
            )
                    .getResultList();

            System.out.println();
            System.out.println("After list select");
            System.out.println();

//            for (Stock stock : list) {
//
//                for (StockDailyRecord stockDailyRecord : stock.getStockDailyRecords()) {
//                    System.out.println(stockDailyRecord.getId());
//                }
//
//            }

            for (StockDailyRecord stockDailyRecord : stockDailyRecords) {
                System.out.println(stockDailyRecord.getStock());
            }

            entityManager.getTransaction().commit();
        }

        entityManager.clear();

        {//однако, если запросов на загрузку коллекций у нас много - то subselect объеденит их в один. в итоге устранив n+1
            entityManager.getTransaction().begin();

            System.out.println();
            System.out.println("Before native list select");
            System.out.println();

            List<Stock> list = entityManager.createNativeQuery("SELECT * FROM stock", Stock.class).getResultList();
//            List<StockDailyRecord> stockDailyRecords = entityManager.createNativeQuery(
//                    "SELECT * FROM stock_daily_record", StockDailyRecord.class
//            )
//                    .getResultList();

            System.out.println();
            System.out.println("After native list select");
            System.out.println();

            for (Stock stock : list) {

                for (StockDailyRecord stockDailyRecord : stock.getStockDailyRecords()) {
                    System.out.println(stockDailyRecord.getId());
                }

            }

//            for (StockDailyRecord stockDailyRecord : stockDailyRecords) {
//                System.out.println(stockDailyRecord.getStock());
//            }

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
        @BatchSize(size = 10)
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

        @OneToMany(mappedBy = "stock", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
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
