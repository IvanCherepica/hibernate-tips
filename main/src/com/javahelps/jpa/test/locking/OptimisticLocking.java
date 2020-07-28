package com.javahelps.jpa.test.locking;

import com.javahelps.jpa.test.util.PersistentHelper;

import javax.persistence.*;

public class OptimisticLocking {
    public static void main(String[] args) throws InterruptedException {
        EntityManager entityManager = PersistentHelper.getEntityManager(new Class[] {Item.class});
        saveData(entityManager);
        entityManager.clear();

        for (int i = 0; i < 5; i++) {
            EntityManager entityManager1 = PersistentHelper.getEntityManager(new Class[] {Item.class});
            new ItemUpdater(entityManager1, "updater 1", 1L).start();
        }

        //Ждем, пока будут выполнены все операции в новых потоках
        Thread.sleep(1000);

        entityManager.clear();

        entityManager.getTransaction().begin();

        Item item = entityManager.find(Item.class, 1L);
        System.out.println();
        System.out.println("sale count - " + item.getSaleCount());
        System.out.println("version - " + item.getVersion());
        System.out.println();

        entityManager.getTransaction().commit();
    }

    static class ItemUpdater extends Thread {

        private long clientId;

        private EntityManager entityManager;

        ItemUpdater(EntityManager entityManager, String name, long clientId){
            super(name);
            this.entityManager = entityManager;
            this.clientId = clientId;
        }

        public void run(){
            entityManager.getTransaction().begin();

            Item item = entityManager.find(Item.class, clientId);
            entityManager.lock(item, LockModeType.OPTIMISTIC);
            int currentSaleCount = item.getSaleCount();
            item.setSaleCount(currentSaleCount+1);
            entityManager.merge(item);

            entityManager.getTransaction().commit();

        }
    }

    private static void saveData(EntityManager entityManager) {
        entityManager.getTransaction().begin();

        Item item1 = new Item("item 1");
        Item item2 = new Item("item 2");
        Item item3 = new Item("item 3");
        Item item4 = new Item("item 4");
        Item item5 = new Item("item 5");
        Item item6 = new Item("item 6");

        entityManager.persist(item1);
        entityManager.persist(item2);
        entityManager.persist(item3);
        entityManager.persist(item4);
        entityManager.persist(item5);
        entityManager.persist(item6);

        entityManager.getTransaction().commit();
    }

    @Entity
    @Table(name = "item")
    private static class Item {

        @Version
        private long version;

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String name;

        private Integer saleCount = 0;

        public Item() {
        }

        public Item(String name) {
            this.name = name;
        }

        public long getVersion() {
            return version;
        }

        public void setVersion(long version) {
            this.version = version;
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

        public Integer getSaleCount() {
            return saleCount;
        }

        public void setSaleCount(Integer saleCount) {
            this.saleCount = saleCount;
        }
    }
}
