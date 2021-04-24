package com.javahelps.jpa.test.locking.isolation.repeatableRead;

import com.javahelps.jpa.test.util.IsolationLevel;
import com.javahelps.jpa.test.util.PersistentHelper;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

public class LostUpdate {

    public static void main(String[] args) {
        PersistentHelper.setIsolationLevel(IsolationLevel.REPEATABLE_READ);
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
