package com.javahelps.jpa.test.n_plus_1_problem_owner_collection.native_query_join_solution;

import com.javahelps.jpa.test.result_set_mapping._5_HibernateSpecificMapping;
import com.javahelps.jpa.test.util.PersistentHelper;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.type.StandardBasicTypes;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class HibernateSpecificMapping {
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

        {//используя FetchMode.SUBSELECT мы и на таком запросе приобретаем один дополнительный запрос
            entityManager.getTransaction().begin();

            System.out.println();
            System.out.println("Before one stock select");
            System.out.println();

            Stock stock = entityManager.find(Stock.class, 1L);

            System.out.println();
            System.out.println("After one stock select");
            System.out.println();

            for (StockDailyRecord stockDailyRecord : stock.getStockDailyRecords()) {
                System.out.println(stockDailyRecord.getId());
            }

            entityManager.getTransaction().commit();
        }

        entityManager.clear();

        {//в случае, когда мы используем коллекцию для инициализации связи - мы не можем ничего указать в качестве алиса для поля
            //через аннотацию @FieldResult. Соответственно, хибернейт не сможет автоматически замапить эту коллекцию, даже не смотря на то
            //что данные уже загружены в память. Более того, если посмотреть на самый последний запрос, проверяющий наличие в кэше
            //первого уровня связанных сущносей - мы увидим, что сущности были распознаны хибернейтом и загружены в кэщ
            //но этого для мапинга недостаточно. Даже если мы сами попытаемся мапить два объекта и потом вставлять коллекции
            //хибернейт воспримит новые объекты как Detached сущности и будет совершать дополнительные запросы каждый раз
            entityManager.getTransaction().begin();

            System.out.println();
            System.out.println("Before list select");
            System.out.println();

            EntityGraph<Stock> entityGraph = entityManager.createEntityGraph(Stock.class);
            entityGraph.addAttributeNodes("stockDailyRecords");


            List<Stock> results = ((Session)entityManager.getDelegate())
                    .createSQLQuery(
                            "SELECT {s.*}, {ssdr.*}, {sdr.*} FROM stock s" +
                                    " JOIN stock_stock_daily_record ssdr ON ssdr.stock_id = s.id " +
                                    " JOIN stock_daily_record sdr ON sdr.id = ssdr.stockDailyRecords_id")
                    .addEntity("s", Stock.class)
//                    .addJoin("ssdr", "s.stockDailyRecords")
                    .addJoin("sdr", "ssdr.stockDailyRecords")
                    .addEntity("s", Stock.class)
                    .setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY)
//                    .addEntity("sdr", StockDailyRecord.class)

                    .list();
//

            System.out.println();
            System.out.println("After list select");
            System.out.println();

            //снова видем n+1 проблему, не смотря на присутствие всех необходимых объектов в кэше
            for (Stock stock : results) {

                for (StockDailyRecord stockDailyRecord : stock.getStockDailyRecords()) {
                    System.out.println(stockDailyRecord.getId());
                }

            }

            entityManager.getTransaction().commit();
        }

        {//используя FetchMode.SUBSELECT мы и на таком запросе приобретаем один дополнительный запрос
            entityManager.getTransaction().begin();

            System.out.println();
            System.out.println("Before one stock select");
            System.out.println();

            Stock stock = entityManager.find(Stock.class, 1L);
            StockDailyRecord stockDailyRecord = entityManager.find(StockDailyRecord.class, 1L);

            System.out.println();
            System.out.println("After one stock select");
            System.out.println();

//            System.out.println(stockDailyRecord.getStock());

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
    }
}
