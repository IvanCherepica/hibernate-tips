package com.javahelps.jpa.test.n_plus_1_problem_many_to_one.eager_solution;

import com.javahelps.jpa.test.n_plus_1_problem_many_to_one.fetch_graph_solution.Graph;
import com.javahelps.jpa.test.util.PersistentHelper;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class EagerFetchType {
    public static void main(String[] args) {
        EntityManager entityManager = PersistentHelper.getEntityManager(new Class[]{Stock.class, StockDailyRecord.class});

        saveData(entityManager);
        entityManager.clear();

        {//если мы достаём запись из базы, используя метод entitymanager, то срабатывает загрузка и данные достаются одним запросом; без n+1
            entityManager.getTransaction().begin();

            System.out.println();
            System.out.println("Before list stock select");
            System.out.println();

            List<StockDailyRecord> list = entityManager.createQuery("from " + StockDailyRecord.class.getName(), StockDailyRecord.class)
                    .getResultList();

            for (StockDailyRecord dailyRecord : list) {
                System.out.println(dailyRecord.getStock());
            }
            System.out.println();
            System.out.println("After list stock select");
            System.out.println();

            entityManager.getTransaction().commit();
        }

        entityManager.clear();

        {//если мы достаём запись из базы, используя метод entitymanager, то срабатывает загрузка и данные достаются одним запросом; без n+1
            entityManager.getTransaction().begin();

            System.out.println();
            System.out.println("Before one stock select");
            System.out.println();

            StockDailyRecord dailyRecord = entityManager.find(StockDailyRecord.class, 1L);
            StockDailyRecord dailyRecordFromQuery = entityManager.createQuery("from " + StockDailyRecord.class.getName() + " WHERE id=2", StockDailyRecord.class)
                    .getSingleResult();

            System.out.println(dailyRecord.getStock());

            System.out.println();
            System.out.println("After one stock select");
            System.out.println();

            entityManager.getTransaction().commit();
        }

//        {//однако, если мы получаем данные, используя запрос - то в таком случае так же будут подгружены сразу все коллекции, но уже в n+1 запросах
//            //для того, что бы исправить эту ситуацию требуется явно задать join fetch
//            entityManager.getTransaction().begin();
//
//            System.out.println();
//            System.out.println("Before list select");
//            System.out.println();
//
//            List<Stock> list = entityManager.createQuery("from " + Stock.class.getName(), Stock.class).getResultList();
//
//            System.out.println();
//            System.out.println("After list select");
//            System.out.println();
//
//            for (Stock stock : list) {
//
//                for (StockDailyRecord stockDailyRecord : stock.getStockDailyRecords()) {
//                    System.out.println(stockDailyRecord.getId());
//                }
//
//            }
//
//            entityManager.getTransaction().commit();
//        }
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

        @ManyToOne(fetch = FetchType.EAGER)
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

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            StockDailyRecord that = (StockDailyRecord) o;
            return id == that.id;
        }

        @Override
        public int hashCode() {
            return Objects.hash(id);
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
