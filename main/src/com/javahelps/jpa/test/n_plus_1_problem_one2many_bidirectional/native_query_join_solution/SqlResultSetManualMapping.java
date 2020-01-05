package com.javahelps.jpa.test.n_plus_1_problem_one2many_bidirectional.native_query_join_solution;

import com.javahelps.jpa.test.util.PersistentHelper;
import org.hibernate.Criteria;
import org.hibernate.Session;

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

        {//убираем n+1 при выборке листа StockDailyRecord
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

            //при подобном способе маппинга - мы можем избежать n+1 проблемы
            for (StockDailyRecord stockDailyRecord : stockDailyRecords) {
                System.out.println(stockDailyRecord.getId());
                System.out.println(stockDailyRecord.getStock());
            }

            entityManager.getTransaction().commit();
        }

        entityManager.clear();

        {//убираем n+1 при выборке листа Stock
            entityManager.getTransaction().begin();

            System.out.println();
            System.out.println("Before list select");
            System.out.println();

            List<Object> objects = entityManager.createNativeQuery(
                    "SELECT s.id AS stockId, sdr.id AS recordId, sdr.stock_id AS stock_id FROM stock s " +
                            "JOIN stock_daily_record sdr ON s.id = sdr.stock_id", "StockRecordMapping")
                    .getResultList();

            Set<Stock> stocks = new HashSet<>(objects.size());

            objects.forEach((record) -> {
                Object[] recordArray = (Object[])record;
                Stock stockDailyRecord = (Stock) recordArray[0];
                stocks.add(stockDailyRecord);
            });

            System.out.println();
            System.out.println("After list select");
            System.out.println();

            //при подобном способе маппинга - мы можем избежать n+1 проблемы
            for (Stock stock : stocks) {
                System.out.println(stock);
                for (StockDailyRecord stockDailyRecord : stock.getStockDailyRecords()) {
                    System.out.println(stockDailyRecord);
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
    @SqlResultSetMappings({
            @SqlResultSetMapping(
                    name = "StockRecordMapping",
                    entities = {
                            @EntityResult(
                                    entityClass = Stock.class,
                                    fields = {
                                            @FieldResult(name = "id", column = "stockId"),
                                            @FieldResult(name = "stockDailyRecords", column = "stockId")
                                    }),
                            @EntityResult(
                                    entityClass = StockDailyRecord.class,
                                    fields = {
                                            @FieldResult(name = "id", column = "recordId"),
                                            @FieldResult(name = "stock", column = "stock_id")
                                    })
                    })
    })
    private static class Stock implements Serializable {

        @Id
        @GeneratedValue
        private long id;

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
