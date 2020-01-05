package com.javahelps.jpa.test.n_plus_1_problem_many_to_one.native_query_solution;

import com.javahelps.jpa.test.util.PersistentHelper;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class SqlResultSetManualMapping {
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

        {//используя @SqlResultSetMapping со связью ManyToOne можно относительно легко попросить хибернейт сделать
            //принудительную выгрузку связанной сущности (в отличии от случая с OneToMany)
            entityManager.getTransaction().begin();

            System.out.println();
            System.out.println("Before list select");
            System.out.println();


            List<Object> objects = entityManager.createNativeQuery(
                    "SELECT s.id AS stockId, sdr.id AS recordId, sdr.stock_id AS stock_id FROM stock_daily_record sdr " +
                            "JOIN stock s ON s.id = sdr.stock_id", "RecordStockMapping")
                    .getResultList();

            Set<StockDailyRecord> stockDailyRecords = new HashSet<>(objects.size());

            objects.forEach((record) -> {
                Object[] recordArray = (Object[])record;
                StockDailyRecord stockDailyRecord = (StockDailyRecord) recordArray[0];
                stockDailyRecords.add(stockDailyRecord);
            });

            System.out.println();
            System.out.println("After list select");
            System.out.println();

            //n+1 проблема устранена
            for (StockDailyRecord stockDailyRecord : stockDailyRecords) {
                System.out.println(stockDailyRecord.getId());
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
    @SqlResultSetMappings({
            @SqlResultSetMapping(
                    name = "RecordStockMapping",
                    entities = {
                            @EntityResult(
                                    entityClass = StockDailyRecord.class,
                                    fields = {
                                            @FieldResult(name = "id", column = "recordId"),
                                            @FieldResult(name = "stock", column = "stock_id")
                                    }),
                            @EntityResult(
                                    entityClass = Stock.class,
                                    fields = {
                                            @FieldResult(name = "id", column = "stockId")
                                    })
                    })
    })
    private static class StockDailyRecord {
        @Id
        @GeneratedValue
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

        public Stock() {
        }

        public void setId(long id) {
            this.id = id;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Stock stock = (Stock) o;
            return id == stock.id;
        }

        @Override
        public int hashCode() {
            return Objects.hash(id);
        }

        @Override
        public String toString() {
            return "Stock{" +
                    "id=" + id +
                    '}';
        }
    }
}
