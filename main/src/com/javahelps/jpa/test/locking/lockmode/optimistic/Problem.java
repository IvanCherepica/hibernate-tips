package com.javahelps.jpa.test.locking.lockmode.optimistic;

import com.javahelps.jpa.test.locking.lockmode.model.Product;
import com.javahelps.jpa.test.locking.lockmode.model.ProductOrder;
import com.javahelps.jpa.test.util.PersistentHelper;

import javax.persistence.EntityManager;


public class Problem {

    public static void main(String[] args) {
        EntityManager entityManager = PersistentHelper.getEntityManager(new Class[] {Product.class, ProductOrder.class});
        saveData(entityManager);
        entityManager.clear();

        //Вводные: Иван хочет заказать все продукты, что сейчас есть на складе. Антон следит, что бы в бд и на складе было одинаковое количество продукта.
        //Антон занимается так же выдачей заказов и может изменять количество любого продукта
        //Учитывая, что на складе в данный момент присутствует только 5 единиц эотго продукта
        //Так же, система регистрирует автора последних изменений по данному продукту

        Product ivanProduct = null;

        {//Начинает Иван. Он запрашивает продукт из базы, что бы составить заказ на его основе
            entityManager.getTransaction().begin();

            ivanProduct = entityManager.find(Product.class, 1L);
        }

        {//Пока Иван меняет данные - Антон замечает неточность. На складе сейчас 0 единиц продукта.
            //Антон исправляет эту неточность и вносит изменения в бд
            EntityManager entityManager2 = PersistentHelper.getEntityManager(new Class[] {Product.class, ProductOrder.class});
            entityManager2.getTransaction().begin();

            Product product = entityManager2.find(Product.class, 1L);
            product.setLastUpdater("Антон");
            product.setItemAmount(0);

            entityManager2.getTransaction().commit();
        }

        {//Иван создает новый заказ, основываясь на старых данных. Иван ждет, что ему придет 5 единц продукта
            //в то время, как на момент создания заказа Иваном в бд уже не было ни одного продукта
            ProductOrder order = new ProductOrder("Иван", ivanProduct.getItemAmount());
            entityManager.persist(order);

            entityManager.getTransaction().commit();
        }
    }

    private static void saveData(EntityManager entityManager) {
        entityManager.getTransaction().begin();

        Product product = new Product("Никто");
        entityManager.persist(product);

        entityManager.getTransaction().commit();
    }
}
