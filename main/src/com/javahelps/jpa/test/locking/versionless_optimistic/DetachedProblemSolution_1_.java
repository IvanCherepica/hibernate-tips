package com.javahelps.jpa.test.locking.versionless_optimistic;

import com.javahelps.jpa.test.util.PersistentHelper;
import org.hibernate.Session;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.OptimisticLockType;
import org.hibernate.annotations.OptimisticLocking;
import org.hibernate.annotations.SelectBeforeUpdate;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

public class DetachedProblemSolution_1_ {

    public static void main(String[] args) throws InterruptedException {
        EntityManager entityManager = PersistentHelper.getEntityManager(new Class[] {DetachedProblemSolution_1_.Product.class});
        saveData(entityManager);
        entityManager.clear();

        //Вводные: два юзера, Иван и Антон хотят забрать несколько единиц одного и того же продукта. Иван хочет забрать одну
        //единицу, а Антон все 5. Учитывая, что на складе в данный момент присутствует только 5 единиц эотго продукта
        //Так же, система регистрирует автора последних изменений по данному продукту

        DetachedProblemSolution_1_.Product ivanProduct = null;

        {//Начинает Иван
            entityManager.getTransaction().begin();

            ivanProduct = entityManager.find(DetachedProblemSolution_1_.Product.class, 1L);

            //моделируем ситуацию, в которой объект был отсоединен от сесси
            entityManager.detach(ivanProduct);

            ivanProduct.setLastUpdater("Иван");
        }

        {//Пока Иван меняет данные - врывается Антон и забирает все 5 имеющихся единиц товара
            EntityManager entityManager2 = PersistentHelper.getEntityManager(new Class[] {DetachedProblemSolution_1_.Product.class});
            entityManager2.getTransaction().begin();

            DetachedProblemSolution_1_.Product product = entityManager2.find(DetachedProblemSolution_1_.Product.class, 1L);
            product.setItemAmount(product.getItemAmount()-5);
            product.setLastUpdater("Антон");
            entityManager2.getTransaction().commit();
        }

        {//Используем метод update сессии для сохранения состояния detached объекта - получаем потерянный update

            ivanProduct.setItemAmount(ivanProduct.getItemAmount()-1);

            entityManager.unwrap(Session.class).update(ivanProduct);
            entityManager.getTransaction().commit();
        }

        entityManager.clear();

        entityManager.getTransaction().begin();

        DetachedProblemSolution_1_.Product product = entityManager.find(DetachedProblemSolution_1_.Product.class, 1L);
        System.out.println(product.getItemAmount());
        System.out.println(product.getLastUpdater());
    }

    private static void saveData(EntityManager entityManager) {
        entityManager.getTransaction().begin();

        DetachedProblemSolution_1_.Product product = new DetachedProblemSolution_1_.Product("item 1");
        entityManager.persist(product);

        entityManager.getTransaction().commit();
    }

    @Entity
    @Table(name = "product")
    @DynamicUpdate
    @OptimisticLocking(type = OptimisticLockType.DIRTY)
    @SelectBeforeUpdate
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

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
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
