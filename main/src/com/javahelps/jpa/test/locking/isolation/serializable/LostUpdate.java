package com.javahelps.jpa.test.locking.isolation.serializable;

import com.javahelps.jpa.test.locking.pessimistic.DetachedSolution;
import com.javahelps.jpa.test.util.IsolationLevel;
import com.javahelps.jpa.test.util.PersistentHelper;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.HashMap;
import java.util.Map;

public class LostUpdate {

    public static void main(String[] args) {
        PersistentHelper.setIsolationLevel(IsolationLevel.SERIALIZABLE);
        EntityManager entityManager = PersistentHelper.getEntityManager(new Class[] {Product.class});
        saveData(entityManager);
        entityManager.clear();

        //Вводные: два юзера, Иван и Антон хотят забрать несколько единиц одного и того же продукта. Иван хочет забрать одну
        //единицу, а Антон все 5. Учитывая, что на складе в данный момент присутствует только 5 единиц эотго продукта
        //Так же, система регистрирует автора последних изменений по данному продукту

        Product ivanProduct = null;

        {//Начинает Иван
            entityManager.getTransaction().begin();

            ivanProduct = entityManager.find(Product.class, 1L);

            ivanProduct.setLastUpdater("Иван");
        }

        Runnable r = ()->{
//            Пока Иван меняет данные - врывается Антон и забирает все 5 имеющихся единиц товара
            EntityManager entityManager2 = PersistentHelper.getEntityManager(new Class[] {Product.class});
            EntityTransaction transaction2 = entityManager2.getTransaction();
            transaction2.begin();

            Product product = entityManager2.find(Product.class, 1L);

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

        {//Иван доделывает свою операцию и комитит изменения
            ivanProduct.setItemAmount(ivanProduct.getItemAmount()-1);

            entityManager.getTransaction().commit();
        }

        entityManager.clear();

        entityManager.getTransaction().begin();

        //На выходе получаем itemAmount 4, вместо 0, т.к. Антон фактически забрал все продукты
        //Последним редактором будет Иван, хотя Антон начал действовать позже.
        //Информация о действиях Антона и результаты этих действий будут утеряны, а Иван получит несуществующую единицу продукта
        Product product = entityManager.find(Product.class, 1L);
        System.out.println(product.getItemAmount());
        System.out.println(product.getLastUpdater());
    }

    private static void saveData(EntityManager entityManager) {
        entityManager.getTransaction().begin();

        Product product = new Product("Никто");
        entityManager.persist(product);

        entityManager.getTransaction().commit();
    }

    @Entity
    @Table(name = "product")
    @Getter
    @Setter
    private static class Product {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String name = "Product";

        private String lastUpdater;

        private Integer itemAmount = 5;

        public Product() {
        }

        public Product(String sallername) {
            this.lastUpdater = sallername;
        }
    }
}
