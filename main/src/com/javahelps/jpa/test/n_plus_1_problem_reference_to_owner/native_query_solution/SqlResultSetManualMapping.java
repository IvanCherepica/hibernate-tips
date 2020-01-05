//package com.javahelps.jpa.test.n_plus_1_problem_reference_to_owner.native_query_solution;
//
//import com.javahelps.jpa.test.util.PersistentHelper;
//
//import javax.persistence.*;
//import java.io.Serializable;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Objects;
//import java.util.Set;
//
//public class SqlResultSetManualMapping {
//    public static void main(String[] args) {
//        EntityManager entityManager = PersistentHelper.getEntityManager(new Class[]{Stock.class, StockDailyRecord.class});
//
//        saveData(entityManager);
//        entityManager.clear();
//
//        {//при использовании ленивой загрузки мы лишаемся необходимости каждый раз загружать в память всю информацию
//            entityManager.getTransaction().begin();
//
//            System.out.println();
//            System.out.println("Before one stock select without collection call");
//            System.out.println();
//
//            Stock stock = entityManager.find(Stock.class, 1L);
//
//            System.out.println();
//            System.out.println("After one stock select without collection call");
//            System.out.println();
//
//            entityManager.getTransaction().commit();
//        }
//
//        entityManager.clear();
//
////        {
////            entityManager.getTransaction().begin();
////
////            System.out.println();
////            System.out.println("Before one stock select");
////            System.out.println();
////
////
////
////            System.out.println();
////            System.out.println("After one stock select");
////            System.out.println();
////
////            for (StockDailyRecord stockDailyRecord : stock.getStockDailyRecords()) {
////                System.out.println(stockDailyRecord.getId());
////            }
////
////            entityManager.getTransaction().commit();
////        }
//
//        entityManager.clear();
//
//        {//пока не нашел ни одного способа заставить работать принудительную загрузку ленивых полей в запросе используя
//            //@SqlResultSetMappings, всегда получаю n+1
//            entityManager.getTransaction().begin();
//
//            System.out.println();
//            System.out.println("Before list select");
//            System.out.println();
//
//
//            List<Object> objects = entityManager.createNativeQuery(
//                    "SELECT s.id AS stockId, sdr.id AS recordId FROM stock s " +
//                            "JOIN stock_stock_daily_record ssdr ON s.id = ssdr.stock_id " +
//                            "JOIN  stock_daily_record sdr ON ssdr.stockDailyRecords_id = sdr.id", "BookAuthorMapping")
//                    .getResultList();
//
//            Set<Stock> stocks = new HashSet<>(objects.size());
//            Set<StockDailyRecord> stockDailyRecords = new HashSet<>(objects.size());
//
//            objects.forEach((record) -> {
//                Object[] recordArray = (Object[])record;
//                Stock stock = (Stock) recordArray[0];
//                StockDailyRecord stockDailyRecord = (StockDailyRecord) recordArray[1];
//                stocks.add(stock);
//                stockDailyRecords.add(stockDailyRecord);
//            });
//
//            //проверяем, были ли объекты загружены в кэш первого
//            for (StockDailyRecord stockDailyRecord : stockDailyRecords) {
//                System.out.println(entityManager.contains(stockDailyRecord));
//            }
//
//
//
//            System.out.println();
//            System.out.println("After list select");
//            System.out.println();
//
//            //снова видем n+1 проблему, не смотря на присутствие всех необходимых объектов в кэше
//            for (Stock stock : stocks) {
//
//                for (StockDailyRecord stockDailyRecord : stock.getStockDailyRecords()) {
//                    System.out.println(stockDailyRecord.getId());
//                }
//
//            }
//
//            entityManager.getTransaction().commit();
//        }
//    }
//
//    private static void saveData(EntityManager entityManager) {
//        entityManager.getTransaction().begin();
//
//        StockDailyRecord stockDailyRecord1 = new StockDailyRecord();
//        StockDailyRecord stockDailyRecord2 = new StockDailyRecord();
//        StockDailyRecord stockDailyRecord3 = new StockDailyRecord();
//
//        StockDailyRecord stockDailyRecord4 = new StockDailyRecord();
//        StockDailyRecord stockDailyRecord5 = new StockDailyRecord();
//        StockDailyRecord stockDailyRecord6 = new StockDailyRecord();
//
//        StockDailyRecord stockDailyRecord7 = new StockDailyRecord();
//        StockDailyRecord stockDailyRecord8 = new StockDailyRecord();
//        StockDailyRecord stockDailyRecord9 = new StockDailyRecord();
//
//        entityManager.persist(stockDailyRecord1);
//        entityManager.persist(stockDailyRecord2);
//        entityManager.persist(stockDailyRecord3);
//
//        entityManager.persist(stockDailyRecord4);
//        entityManager.persist(stockDailyRecord5);
//        entityManager.persist(stockDailyRecord6);
//
//        entityManager.persist(stockDailyRecord7);
//        entityManager.persist(stockDailyRecord8);
//        entityManager.persist(stockDailyRecord9);
//
//        Stock stock1 = new Stock();
//        Stock stock2 = new Stock();
//        Stock stock3 = new Stock();
//
//        entityManager.persist(stock1);
//        entityManager.persist(stock2);
//        entityManager.persist(stock3);
//
//        stockDailyRecord1.setStock(stock1);
//        stockDailyRecord2.setStock(stock1);
//        stockDailyRecord3.setStock(stock1);
//
//        stockDailyRecord4.setStock(stock2);
//        stockDailyRecord5.setStock(stock2);
//        stockDailyRecord6.setStock(stock2);
//
//        stockDailyRecord7.setStock(stock3);
//        stockDailyRecord8.setStock(stock3);
//        stockDailyRecord9.setStock(stock3);
//
//        entityManager.getTransaction().commit();
//    }
//
//
//
//    @Entity
//    @Table(name = "stock_daily_record")
//    private static class StockDailyRecord {
//        @Id
//        @GeneratedValue
//        private long id;
//
//        @ManyToOne(fetch = FetchType.LAZY)
//        @JoinColumn(name = "stock_id")
//        private Stock stock;
//
//        public StockDailyRecord() {
//        }
//
//        public long getId() {
//            return id;
//        }
//
//        public void setId(long id) {
//            this.id = id;
//        }
//
//        public Stock getStock() {
//            return stock;
//        }
//
//        public void setStock(Stock stock) {
//            this.stock = stock;
//        }
//
//        @Override
//        public boolean equals(Object o) {
//            if (this == o) return true;
//            if (o == null || getClass() != o.getClass()) return false;
//            StockDailyRecord that = (StockDailyRecord) o;
//            return id == that.id;
//        }
//
//        @Override
//        public int hashCode() {
//            return Objects.hash(id);
//        }
//    }
//
//    @Entity
//    @Table(name = "stock")
//    @SqlResultSetMappings({
//            @SqlResultSetMapping(
//                    name = "AuthorMapping",
//                    entities = @EntityResult(
//                            entityClass = Stock.class,
//                            fields = {
//                                    @FieldResult(name = "id", column = "stockId")
//                            }
//                    )
//            ),
//            @SqlResultSetMapping(
//                    name = "BookAuthorMapping",
//                    entities = {
//                            @EntityResult(
//                                    entityClass = Stock.class,
//                                    fields = {
//                                            @FieldResult(name = "id", column = "stockId")
//                                    }),
//                            @EntityResult(
//                                    entityClass = StockDailyRecord.class,
//                                    fields = {
//                                            @FieldResult(name = "id", column = "recordId")
//                                    })
//                    })
//    })
//    private static class Stock implements Serializable {
//
//        @Id
//        @GeneratedValue
//        private long id;
//
//        public Stock() {
//        }
//
//        public void setId(long id) {
//            this.id = id;
//        }
//
//        @Override
//        public boolean equals(Object o) {
//            if (this == o) return true;
//            if (o == null || getClass() != o.getClass()) return false;
//            Stock stock = (Stock) o;
//            return id == stock.id;
//        }
//
//        @Override
//        public int hashCode() {
//            return Objects.hash(id);
//        }
//    }
//}
