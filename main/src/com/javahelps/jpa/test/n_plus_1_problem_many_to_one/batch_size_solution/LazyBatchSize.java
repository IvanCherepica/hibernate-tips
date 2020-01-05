package com.javahelps.jpa.test.n_plus_1_problem_many_to_one.batch_size_solution;

import com.javahelps.jpa.test.util.PersistentHelper;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LazyBatchSize {
    public static void main(String[] args) {
        EntityManager entityManager = PersistentHelper.getEntityManager(new Class[]{Stock.class, StockDailyRecord.class});

        saveData(entityManager);
        entityManager.clear();

        {//самое плохое в использовании Eager или fetchMode Join - коллекция всегда будет загружена в память принудительно
            //вне зависимости от того - намереваемся ли мы эту коллекцию испльзовать дальше, или нет
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
            System.out.println("Before one stock select");
            System.out.println();

            StockDailyRecord stockDailyRecord = entityManager.find(StockDailyRecord.class, 1L);

            System.out.println();
            System.out.println("After one stock select");
            System.out.println();

            System.out.println(stockDailyRecord.getStock());

            entityManager.getTransaction().commit();
        }

        entityManager.clear();

        {//указывая BatchSize мы говорим хибернейту, сколько коллекций надо будет загруить при обращении.
            //т.е. сначала будет так же произведен запрос на выборку листа Stocs и уже после отправится ещё один запрос
            //уже на выборку коллекций. Если число, указанное в аннотации BatchSize больше или равно реальному числу
            //связанных с объектом коллекций - будет отправлен только один запрос.
            //Если число, указанное в batch size меньше чем число связанных с объектом коллекций, то будут производиться доплнительные запросы
            //до тех пор, пока не будет выбрана свя информация из базы. Таким образом аннотация
            //BatchSize не устраняет проблему n+1, а позволяет сгладить негативныое влияние дополнительных запросов
            //(n/b)+1, где b это число, указанное в batch size
            entityManager.getTransaction().begin();

            System.out.println();
            System.out.println("Before list select");
            System.out.println();

            List<StockDailyRecord> list = entityManager.createQuery("from " + StockDailyRecord.class.getName(), StockDailyRecord.class).getResultList();

            System.out.println();
            System.out.println("After list select");
            System.out.println();

            for (StockDailyRecord stockDailyRecord : list) {
                System.out.println(stockDailyRecord.getStock());
            }

            entityManager.getTransaction().commit();
        }

        entityManager.clear();

        {//та же самая история происхоит при использовании нативных запросов
            entityManager.getTransaction().begin();

            System.out.println();
            System.out.println("Before list native select");
            System.out.println();

            List<StockDailyRecord> list = entityManager.createNativeQuery("SELECT * FROM stock_daily_record", StockDailyRecord.class).getResultList();

            System.out.println();
            System.out.println("After list  native select");
            System.out.println();

            for (StockDailyRecord stockDailyRecord : list) {
                System.out.println(stockDailyRecord.getStock());
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
