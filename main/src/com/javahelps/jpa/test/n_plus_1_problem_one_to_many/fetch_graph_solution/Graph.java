package com.javahelps.jpa.test.n_plus_1_problem_one_to_many.fetch_graph_solution;

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

            Stock stock = entityManager.find(Stock.class, 1L);

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

            EntityGraph<Stock> entityGraph = entityManager.createEntityGraph(Stock.class);
            entityGraph.addAttributeNodes("stockDailyRecords");

            Map<String, Object> hints = new HashMap<>();
            hints.put("javax.persistence.loadgraph", entityGraph);

            Stock stock = entityManager.find(Stock.class, 1L, hints);


            System.out.println();
            System.out.println("After one stock with graph select");
            System.out.println();

            for (StockDailyRecord stockDailyRecord : stock.getStockDailyRecords()) {
                System.out.println(stockDailyRecord.getId());
            }

            entityManager.getTransaction().commit();
        }

        entityManager.clear();

        {//применяя фечграф к запросу, мы говорим хибернейту, как и с какими нодами надо будет загрузить все объекты
            entityManager.getTransaction().begin();

            System.out.println();
            System.out.println("Before list with graph select");
            System.out.println();

            EntityGraph<Stock> entityGraph = entityManager.createEntityGraph(Stock.class);
            entityGraph.addAttributeNodes("stockDailyRecords");

            List<Stock> list = entityManager.createQuery("from " + Stock.class.getName(), Stock.class)
                    .setHint("javax.persistence.loadgraph", entityGraph)
                    .getResultList();

            System.out.println();
            System.out.println("After list with graph select");
            System.out.println();

            for (Stock stock : list) {

                for (StockDailyRecord stockDailyRecord : stock.getStockDailyRecords()) {
                    System.out.println(stockDailyRecord.getId());
                }

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

        Set<StockDailyRecord> stockDailyRecords1 = new HashSet<>();
        stockDailyRecords1.add(stockDailyRecord1);
        stockDailyRecords1.add(stockDailyRecord2);
        stockDailyRecords1.add(stockDailyRecord3);

        Set<StockDailyRecord> stockDailyRecords2 = new HashSet<>();
        stockDailyRecords2.add(stockDailyRecord4);
        stockDailyRecords2.add(stockDailyRecord5);
        stockDailyRecords2.add(stockDailyRecord6);

        Set<StockDailyRecord> stockDailyRecords3 = new HashSet<>();
        stockDailyRecords3.add(stockDailyRecord7);
        stockDailyRecords3.add(stockDailyRecord8);
        stockDailyRecords3.add(stockDailyRecord9);

        stock1.setStockDailyRecords(stockDailyRecords1);
        stock2.setStockDailyRecords(stockDailyRecords2);
        stock3.setStockDailyRecords(stockDailyRecords3);

        entityManager.getTransaction().commit();
    }


    @Entity
    @Table(name = "stock_daily_record")
    private static class StockDailyRecord {
        @Id
        @GeneratedValue
        private long id;

        public StockDailyRecord() {
        }

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

    }

    @Entity
    @Table(name = "stock")
    private static class Stock implements Serializable {

        @Id
        @GeneratedValue
        private long id;

        @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
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
