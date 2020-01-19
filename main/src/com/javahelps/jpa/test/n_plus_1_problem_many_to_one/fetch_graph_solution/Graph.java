package com.javahelps.jpa.test.n_plus_1_problem_many_to_one.fetch_graph_solution;

import com.javahelps.jpa.test.util.PersistentHelper;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

public class Graph {
    public static void main(String[] args) {
        EntityManager entityManager = PersistentHelper.getEntityManager(new Class[]{Stock.class, StockDailyRecord.class});

        saveData(entityManager);
        entityManager.clear();

        {//при использовании ленивой загрузки мы лишаемся необходимости каждый раз загружать в память всю информацию
            entityManager.getTransaction().begin();

            System.out.println();
            System.out.println("Before one stock select without collection call");
            System.out.println();

            StockDailyRecord stockDailyRecord = entityManager.find(StockDailyRecord.class, 1L);

            System.out.println();
            System.out.println("After one stock select without collection call");
            System.out.println();

            entityManager.getTransaction().commit();
        }

        entityManager.clear();

        {//говорим хибернейту, что нужно в режиме EAGER подтянуть всё, что указано в фечграфе
            entityManager.getTransaction().begin();

            System.out.println();
            System.out.println("Before one stock with graph select");
            System.out.println();

            EntityGraph<StockDailyRecord> entityGraph = entityManager.createEntityGraph(StockDailyRecord.class);
            entityGraph.addAttributeNodes("stock");

            Map<String, Object> hints = new HashMap<>();
            hints.put("javax.persistence.loadgraph", entityGraph);

            StockDailyRecord stockDailyRecord = entityManager.find(StockDailyRecord.class, 1L, hints);

            System.out.println();
            System.out.println("After one stock with graph select");
            System.out.println();

            System.out.println(stockDailyRecord.getStock());

            entityManager.getTransaction().commit();
        }

        entityManager.clear();

        {//применяя фечграф к запросу, мы говорим хибернейту, как и с какими нодами надо будет загрузить все объекты
            entityManager.getTransaction().begin();

            System.out.println();
            System.out.println("Before list with graph select");
            System.out.println();

            EntityGraph<StockDailyRecord> entityGraph = entityManager.createEntityGraph(StockDailyRecord.class);
            entityGraph.addAttributeNodes("stock");

            List<StockDailyRecord> list = entityManager.createQuery("from " + StockDailyRecord.class.getName(), StockDailyRecord.class)
                    .setHint("javax.persistence.loadgraph", entityGraph)
                    .getResultList();

            System.out.println();
            System.out.println("After list with graph select");
            System.out.println();

            for (StockDailyRecord stockDailyRecord : list) {
                System.out.println(stockDailyRecord.getStock());
            }


            entityManager.getTransaction().commit();
        }

        entityManager.clear();

        {//невозможно применить графы для нативных запросов
            entityManager.getTransaction().begin();

            System.out.println();
            System.out.println("Before list native select");
            System.out.println();

            List<Stock> list = entityManager.createNativeQuery("SELECT * FROM stock", Stock.class).getResultList();

            System.out.println();
            System.out.println("After list  native select");
            System.out.println();

//            for (Stock stock : list) {
//
//                for (StockDailyRecord stockDailyRecord : stock.getStockDailyRecords()) {
//                    System.out.println(stockDailyRecord);
//                }
//
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

        stockDailyRecord1.setStock(stock1);
        stockDailyRecord2.setStock(stock1);
        stockDailyRecord3.setStock(stock1);

        stockDailyRecord4.setStock(stock2);
        stockDailyRecord5.setStock(stock2);
        stockDailyRecord6.setStock(stock2);

        stockDailyRecord7.setStock(stock3);
        stockDailyRecord8.setStock(stock3);
        stockDailyRecord9.setStock(stock3);

        entityManager.getTransaction().commit();
    }


    @Entity
    @Table(name = "stock_daily_record")
    private static class StockDailyRecord {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private long id;

        @ManyToOne(fetch = FetchType.LAZY)
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
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private long id;

        public Stock() {
        }

        public void setId(long id) {
            this.id = id;
        }

        @Override
        public String toString() {
            return "Stock{" +
                    "id=" + id +
                    '}';
        }
    }
}
