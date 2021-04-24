package com.javahelps.jpa.test.locking.pessimistic;

import com.javahelps.jpa.test.util.PersistentHelper;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.OptimisticLockType;
import org.hibernate.annotations.OptimisticLocking;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.LockModeType;
import javax.persistence.Table;
import java.util.HashMap;
import java.util.Map;

public class DetachedSolution {
    
    public static void main(String[] args) throws InterruptedException {
        EntityManager entityManager = PersistentHelper.getEntityManager(new Class[] {DetachedSolution.Product.class});
        saveData(entityManager);
        entityManager.clear();

        //Вводные: два юзера, Иван и Антон хотят забрать несколько единиц одного и того же продукта. Иван хочет забрать одну
        //единицу, а Антон все 5. Учитывая, что на складе в данный момент присутствует только 5 единиц эотго продукта
        //Так же, система регистрирует автора последних изменений по данному продукту

        DetachedSolution.Product ivanProduct = null;
        EntityTransaction transaction = entityManager.getTransaction();
        transaction.begin();

        {//Начинает Иван

            Map<String, Object> properties = new HashMap<>();
            properties.put("javax.persistence.lock.timeout", 10L);

            ivanProduct = entityManager.find(DetachedSolution.Product.class, 1L, LockModeType.PESSIMISTIC_WRITE);

            //моделируем ситуацию, в которой объект был отсоединен от сесси
            entityManager.detach(ivanProduct);

            ivanProduct.setLastUpdater("Иван");
        }

        Runnable r = ()->{
//            Пока Иван меняет данные - врывается Антон и забирает все 5 имеющихся единиц товара

            Map<String, Object> properties1 = new HashMap<>();
            properties1.put("javax.persistence.lock.timeout", 10L);

            EntityManager entityManager2 = PersistentHelper.getEntityManager(new Class[] {DetachedSolution.Product.class});
            EntityTransaction transaction2 = entityManager2.getTransaction();
            transaction2.begin();

            System.out.println();
            System.out.println(transaction.equals(transaction2));
            System.out.println();

            DetachedSolution.Product product = entityManager2.find(DetachedSolution.Product.class, 1L);

            entityManager2.detach(product);

            product.setItemAmount(product.getItemAmount()-5);
            product.setLastUpdater("Антон");

            entityManager.merge(product);

            transaction2.commit();
        };

        Thread myThread = new Thread(r,"MyThread");
        myThread.start();

        try{
            Thread.sleep(500);
        }
        catch(InterruptedException e){
            System.out.println("Thread has been interrupted");
        }


        {//Используем метод update сессии для сохранения состояния detached объекта - получаем потерянный update

            ivanProduct.setItemAmount(ivanProduct.getItemAmount()-1);

            entityManager.merge(ivanProduct);
            entityManager.getTransaction().commit();
        }

        entityManager.clear();

        entityManager.getTransaction().begin();

        DetachedSolution.Product product = entityManager.find(DetachedSolution.Product.class, 1L);
        System.out.println(product.getItemAmount());
        System.out.println(product.getLastUpdater());
    }

    private static void saveData(EntityManager entityManager) {
        entityManager.getTransaction().begin();

        DetachedSolution.Product product = new DetachedSolution.Product("item 1");
        entityManager.persist(product);

        entityManager.getTransaction().commit();
    }

    @Entity
    @Table(name = "product")
    @DynamicUpdate
    @OptimisticLocking(type = OptimisticLockType.DIRTY)
    private static class Product {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String name = "Product";

        private String lastUpdater;

        private Integer itemAmount = 5;

        public Product() {
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Product(String sallername) {
            this.lastUpdater = sallername;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getLastUpdater() {
            return lastUpdater;
        }

        public void setLastUpdater(String lastUpdater) {
            this.lastUpdater = lastUpdater;
        }

        public Integer getItemAmount() {
            return itemAmount;
        }

        public void setItemAmount(Integer itemAmount) {
            this.itemAmount = itemAmount;
        }
    }
}
