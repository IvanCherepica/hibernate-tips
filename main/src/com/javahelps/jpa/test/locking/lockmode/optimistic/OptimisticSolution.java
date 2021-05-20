package com.javahelps.jpa.test.locking.lockmode.optimistic;

import com.javahelps.jpa.test.locking.lockmode.CustomInterceptor;
import com.javahelps.jpa.test.locking.lockmode.model.Product;
import com.javahelps.jpa.test.locking.lockmode.model.ProductOrder;
import com.javahelps.jpa.test.util.PersistentHelper;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.LockModeType;
import javax.persistence.Table;
import javax.persistence.Version;

public class OptimisticSolution {

    public static void main(String[] args) {
        EntityManager entityManager = PersistentHelper.getEntityManager(new Class[] {Product.class, ProductOrder.class});
        saveData(entityManager);
        entityManager.clear();

        //Вводные: Иван хочет заказать все продукты, что сейчас есть на складе. Антон следит, что бы в бд и на складе было одинаковое количество продукта.
        //Антон занимается так же выдачей заказов и может изменять количество любого продукта
        //Учитывая, что на складе в данный момент присутствует только 5 единиц эотго продукта
        //Так же, система регистрирует автора последних изменений по данному продукту

        Product ivanProduct = null;

        {//Начинает Иван. Он запрашивает продукт из базы, что бы составить заказ на его основе. Однако, на этот раз
            //Иван примеряет оптимистичную блокировку к продукту, на основе которого он будет составлять заказ
            entityManager.getTransaction().begin();

            ivanProduct = entityManager.find(Product.class, 1L);
            entityManager.lock(ivanProduct, LockModeType.OPTIMISTIC);
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

        {//Перед тем, как закомитить изменения, транзакция ивана проверит значение поля version у всех заблокированных сущностей
            //если значение хоть у одной будет отличаться - транзакция выполнит rollback
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
