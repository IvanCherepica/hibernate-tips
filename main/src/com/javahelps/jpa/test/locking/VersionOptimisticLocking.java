package com.javahelps.jpa.test.locking;

import com.javahelps.jpa.test.util.PersistentHelper;

import javax.persistence.*;

public class VersionOptimisticLocking {
    public static void main(String[] args) throws InterruptedException {
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

        {//Пока Иван меняет данные - врывается Антон и забирает все 5 имеющихся единиц товара
            EntityManager entityManager2 = PersistentHelper.getEntityManager(new Class[] {Product.class});
            entityManager2.getTransaction().begin();

            Product product = entityManager2.find(Product.class, 1L);
            product.setLastUpdater("Антон");
            product.setItemAmount(product.getItemAmount()-5);

            entityManager2.getTransaction().commit();
        }

        {//Иван доделывает свою операцию и комитит изменения - на выходе получает ошибку, т.к. его транзакция на момент
            //комита имеет неактуальные данные. В этом случае Ивану нужно снова запросить данные и повторить изменения
            ivanProduct.setItemAmount(ivanProduct.getItemAmount()-1);

            entityManager.getTransaction().commit();
        }

        entityManager.clear();

        entityManager.getTransaction().begin();

        Product product = entityManager.find(Product.class, 1L);
        System.out.println(product.getItemAmount());
        System.out.println(product.getLastUpdater());
    }

    private static void saveData(EntityManager entityManager) {
        entityManager.getTransaction().begin();

        Product product = new Product("item 1");
        entityManager.persist(product);

        entityManager.getTransaction().commit();
    }

    @Entity
    @Table(name = "product")
    private static class Product {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String name = "Product";

        private String lastUpdater;

        private Integer itemAmount = 5;

        @Version
        private Integer version;

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
