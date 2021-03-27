package com.javahelps.jpa.test.locking.versionless_optimistic;

import com.javahelps.jpa.test.util.PersistentHelper;
import org.hibernate.Session;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.OptimisticLockType;
import org.hibernate.annotations.OptimisticLocking;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

public class NonOverlappingSolution {

    public static void main(String[] args) throws InterruptedException {
        EntityManager entityManager = PersistentHelper.getEntityManager(new Class[] {NonOverlappingSolution.Product.class});
        saveData(entityManager);
        entityManager.clear();

        //Вводные: на этот раз просто две тразакции, каждая из которых меняет своё определенное поле

        NonOverlappingSolution.Product ivanProduct = null;

        {//Начинает Иван и меняет только значение lastUpdater
            entityManager.getTransaction().begin();

            ivanProduct = entityManager.find(NonOverlappingSolution.Product.class, 1L);

            ivanProduct.setLastUpdater("Иван");
        }

        {//Вторая тразнакция перехватывает инциативу, но не меняет значение поля lastUpdater. Вместо этого она меняет itemAmount
            EntityManager entityManager2 = PersistentHelper.getEntityManager(new Class[] {NonOverlappingSolution.Product.class});
            entityManager2.getTransaction().begin();

            NonOverlappingSolution.Product product = entityManager2.find(NonOverlappingSolution.Product.class, 1L);
            product.setItemAmount(product.getItemAmount()-5);

            entityManager2.getTransaction().commit();
        }

        {//Не меняем itemAmount, но всё равно получаем ошибку
            entityManager.getTransaction().commit();
        }

        entityManager.clear();

        entityManager.getTransaction().begin();

        NonOverlappingSolution.Product product = entityManager.find(NonOverlappingSolution.Product.class, 1L);
        System.out.println(product.getItemAmount());
        System.out.println(product.getLastUpdater());
    }

    private static void saveData(EntityManager entityManager) {
        entityManager.getTransaction().begin();

        NonOverlappingSolution.Product product = new NonOverlappingSolution.Product("item 1");
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

        public Product(String sallername) {
            this.lastUpdater = sallername;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
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
