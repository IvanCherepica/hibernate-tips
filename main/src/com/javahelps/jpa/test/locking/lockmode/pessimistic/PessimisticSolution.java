package com.javahelps.jpa.test.locking.lockmode.pessimistic;

import com.javahelps.jpa.test.locking.lockmode.CustomInterceptor;
import com.javahelps.jpa.test.locking.lockmode.model.Product;
import com.javahelps.jpa.test.locking.lockmode.model.ProductOrder;
import com.javahelps.jpa.test.util.PersistentHelper;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;

public class PessimisticSolution {

    public static void main(String[] args) {
        //Создаем перехватчик, задачей которого будет изменить таблицу product перед select version для сравнения
        CustomInterceptor interceptor = new CustomInterceptor();
        PersistentHelper.setInterceptor(interceptor);
        EntityManager entityManager = PersistentHelper.getEntityManager(new Class[] {Product.class, ProductOrder.class});
        saveData(entityManager);
        entityManager.clear();
        System.out.println();

        //Вводные: Иван хочет взять 5 единиц продукта. Иван делает заказ, т.к. на складе есть такое количество продуктов

        Product ivanProduct = null;

        {//Начинает Иван. Он запрашивает продукт из базы, что бы составить заказ на его основе. Однако, на этот раз
            //Иван примеряет оптимистичную блокировку к продукту, на основе которого он будет составлять заказ
            entityManager.getTransaction().begin();

            ivanProduct = entityManager.find(Product.class, 1L);
            entityManager.lock(ivanProduct, LockModeType.PESSIMISTIC_READ);
        }

        {//Перед тем, как закомитить изменения, транзакция ивана проверит значение поля version у всех заблокированных сущностей
            //если значение хоть у одной будет отличаться - транзакция выполнит rollback
            //Однако теперь перехватчик изменит состояние таблицы product перед комитом транзакции
            ProductOrder order = new ProductOrder("Иван", ivanProduct.getItemAmount());
            entityManager.persist(order);
            interceptor.setReady(true); //говорим перехватчику, что он может начинать действовать

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
