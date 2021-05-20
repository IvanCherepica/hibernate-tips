package com.javahelps.jpa.test.locking.lockmode;

import com.javahelps.jpa.test.locking.lockmode.model.Product;
import com.javahelps.jpa.test.locking.lockmode.model.ProductOrder;
import com.javahelps.jpa.test.locking.lockmode.pessimistic.OptimisticProblem;
import com.javahelps.jpa.test.util.PersistentHelper;
import org.hibernate.EmptyInterceptor;
import org.hibernate.Transaction;

import javax.persistence.EntityManager;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Этот интерцептор нужен для того, что бы перед завершением транзакции изменить состояние таблицы product
 * */
public class CustomInterceptor extends EmptyInterceptor {

    private AtomicBoolean ready = new AtomicBoolean();

    private final Class[] classes = new Class[] {
           Product.class,
           ProductOrder.class
    };

    @Override
    public void beforeTransactionCompletion(Transaction tx) {
        if (ready.get()) {
            Thread myThread = new Thread(r,"Interceptor thread");
            myThread.start();

            ready.set(false);

            try{
                Thread.sleep(500);
                System.out.println();
            }
            catch(InterruptedException e){
                System.out.println("Thread has been interrupted");
            }
        }
    }

    Runnable r = ()->{
        EntityManager entityManager = PersistentHelper.getEntityManager(classes);
        entityManager.getTransaction().begin();
        entityManager.createQuery(
                "UPDATE " + Product.class.getName() + " p SET p.itemAmount = p.itemAmount-5, p.version = p.version+1 WHERE p.id = 1"
        )
                .executeUpdate();
        entityManager.getTransaction().commit();

        try {
            System.out.println("Wait 500 ms for lock to be acquired!");
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }
    };

    public void setReady(boolean state) {
        this.ready.set(state);
    }
}