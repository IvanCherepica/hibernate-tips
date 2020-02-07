package com.javahelps.jpa.test.hash_map_mapping;

import com.javahelps.jpa.test.util.PersistentHelper;

import javax.persistence.*;
import java.io.Serializable;
import java.util.*;

public class AlternativeMapping {
    public static void main(String[] args) {
        EntityManager entityManager = PersistentHelper.getEntityManager(new Class[] {Order.class, Seller.class, Item.class, OrderItemMapping.class});

        saveData(entityManager);
        entityManager.clear();

        {
            System.out.println();
            System.out.println("Before order loaded");
            System.out.println();

            entityManager.getTransaction().begin();

            Order order = entityManager.find(Order.class, 1L);

            for (OrderItemMapping orderItemMapping : order.getOrderItemMappings()) {
                System.out.println(orderItemMapping.getItem().getName());
                System.out.println(orderItemMapping.getSeller().getName());
            }

            entityManager.getTransaction().commit();

            System.out.println();
            System.out.println("After order loaded");
            System.out.println();
        }

        entityManager.clear();

        {   //куча n+1, которые ты никак не поправишь, используя JOIN FETCH
            System.out.println();
            System.out.println("Before order loaded by jpql + join fetch");
            System.out.println();

            entityManager.getTransaction().begin();

            Order order = entityManager.createQuery(
                    "SELECT o FROM "+Order.class.getName()+" o " +
                            "JOIN FETCH o.orderItemMappings" +
                            " WHERE o.id =:id",
                    Order.class
            )
                    .setParameter("id", 1L)
                    .getSingleResult();

            for (OrderItemMapping orderItemMapping : order.getOrderItemMappings()) {
                System.out.println(orderItemMapping.getItem().getName());
                System.out.println(orderItemMapping.getSeller().getName());
            }

            entityManager.getTransaction().commit();

            System.out.println();
            System.out.println("After order loaded by jpql + join fetch");
            System.out.println();
        }

        entityManager.clear();

        {   //хотя на запрос непосредственно проблемно сущности - будет работать
            System.out.println();
            System.out.println("Before OrderItemMapping loaded by jpql + join fetch");
            System.out.println();

            entityManager.getTransaction().begin();

            List<OrderItemMapping> orderItemMappings = entityManager.createQuery(
                    "SELECT oim FROM "+OrderItemMapping.class.getName()+" oim " +
                            "JOIN FETCH oim.seller " +
                            "JOIN FETCH oim.item " +
                            " WHERE oim.order.id =:id",
                    OrderItemMapping.class
            )
                    .setParameter("id", 1L)
                    .getResultList();

            for (OrderItemMapping orderItemMapping : orderItemMappings) {
                System.out.println(orderItemMapping.getItem().getName());
                System.out.println(orderItemMapping.getSeller().getName());
            }

            entityManager.getTransaction().commit();

            System.out.println();
            System.out.println("After OrderItemMapping loaded by jpql + join fetch");
            System.out.println();
        }

        entityManager.clear();

        {   //куча n+1, которые ты никак не поправишь, используя JOIN FETCH
            System.out.println();
            System.out.println("Before order loaded by entity graph");
            System.out.println();

            entityManager.getTransaction().begin();

            EntityGraph<Order> entityGraph = entityManager.createEntityGraph(Order.class);
            Subgraph<OrderItemMapping> itemGraph = entityGraph.addSubgraph("orderItemMappings");
            itemGraph.addAttributeNodes("seller");
            itemGraph.addAttributeNodes("item");

            Map<String, Object> hints = new HashMap<>();
            hints.put("javax.persistence.loadgraph", entityGraph);

            Order order = entityManager.find(Order.class, 1L, hints);

            for (OrderItemMapping orderItemMapping : order.getOrderItemMappings()) {
                System.out.println(orderItemMapping.getItem().getName());
                System.out.println(orderItemMapping.getSeller().getName());
            }

            entityManager.getTransaction().commit();

            System.out.println();
            System.out.println("After order loaded by entity graph");
            System.out.println();
        }
    }

    private static void saveData(EntityManager entityManager) {
        entityManager.getTransaction().begin();

        Seller seller1 = new Seller("seller 1");
        Item item1 = new Item("item 1");
        Seller seller2 = new Seller("seller 2");
        Item item2 = new Item("item 2");
        Seller seller3 = new Seller("seller 3");
        Item item3 = new Item("item 3");

        Order order = new Order("order 1");

        entityManager.persist(seller1);
        entityManager.persist(item1);
        entityManager.persist(seller2);
        entityManager.persist(item2);
        entityManager.persist(seller3);
        entityManager.persist(item3);

        OrderItemMapping orderItemMapping1 = new OrderItemMapping(seller1, item1);
        OrderItemMapping orderItemMapping2 = new OrderItemMapping(seller2, item2);
        OrderItemMapping orderItemMapping3 = new OrderItemMapping(seller3, item3);

        entityManager.persist(order);

        entityManager.persist(orderItemMapping1);
        entityManager.persist(orderItemMapping2);
        entityManager.persist(orderItemMapping3);

        order.addOrderItemMapping(orderItemMapping1);
        order.addOrderItemMapping(orderItemMapping2);
        order.addOrderItemMapping(orderItemMapping3);

        entityManager.getTransaction().commit();
    }

    @Entity
    @Table(name = "orders")
    public static class Order {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id")
        private Long id;

        @Column(name = "name")
        private String name;

        @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
        private List<OrderItemMapping> orderItemMappings = new ArrayList<>();

        public Order() {
        }

        public Order(String name) {
            this.name = name;
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

        //заводим специальные методы для добавления и удаления, которые позволяют избавиться от повторного кода
        public void addOrderItemMapping(OrderItemMapping orderItemMapping) {
            this.orderItemMappings.add(orderItemMapping);
            orderItemMapping.setOrder(this);
        }

        public void removeOrderItemMapping(OrderItemMapping orderItemMapping) {
            this.orderItemMappings.remove(orderItemMapping);
            orderItemMapping.setOrder(null);
        }

        public List<OrderItemMapping> getOrderItemMappings() {
            return orderItemMappings;
        }

        public void setOrderItemMappings(List<OrderItemMapping> orderItemMappings) {
            this.orderItemMappings = orderItemMappings;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Order order = (Order) o;
            return Objects.equals(id, order.id) &&
                    Objects.equals(name, order.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id, name);
        }
    }

    @Entity
    @Table(name = "order_item_mapping")
    public static class OrderItemMapping {

        @Embeddable
        public static class Id implements Serializable {

            @Column(name = "seller_id")
            protected Long sellerId = 0L;

            @Column(name = "item_id")
            protected Long itemId;

            public Id() {
            }

            public Long getSellerId() {
                return sellerId;
            }

            public void setSellerId(Long sellerId) {
                this.sellerId = sellerId;
            }

            public Long getItemId() {
                return itemId;
            }

            public void setItemId(Long itemId) {
                this.itemId = itemId;
            }

            @Override
            public boolean equals(Object o) {
                if (this == o) return true;
                if (o == null || getClass() != o.getClass()) return false;
                Id id = (Id) o;
                return Objects.equals(sellerId, id.sellerId) &&
                        Objects.equals(itemId, id.itemId);
            }

            @Override
            public int hashCode() {
                return Objects.hash(sellerId, itemId);
            }
        }

        @EmbeddedId
        private Id id = new Id();

        @OneToOne(targetEntity = Seller.class, fetch = FetchType.LAZY)
        @JoinColumn(name = "seller_id",
                insertable = false, updatable = false)
        private Seller seller;

        @OneToOne(targetEntity = Item.class, fetch = FetchType.LAZY)
        @JoinColumn(name = "item_id", insertable = false, updatable = false)
        private Item item;

        @ManyToOne(targetEntity = Order.class, fetch = FetchType.LAZY)
        @JoinColumn(name = "order_id")
        private Order order;

        public OrderItemMapping() {
        }

        public OrderItemMapping(Seller seller, Item item) {
            this.seller = seller;
            this.item = item;
            this.id.sellerId = seller.getId();
            this.id.itemId = item.getId();
        }

        public Id getId() {
            return id;
        }

        public void setId(Id id) {
            this.id = id;
        }

        public Order getOrder() {
            return order;
        }

        public void setOrder(Order order) {
            this.order = order;
        }

        public Seller getSeller() {
            return seller;
        }

        public void setSeller(Seller seller) {
            if (seller == null){
                this.seller = new Seller();
            } else {
                this.seller = seller;
                this.id.sellerId = seller.getId();
            }
        }

        public Item getItem() {
            return item;
        }

        public void setItem(Item item) {
            if (item == null){
                this.item = new Item();
            } else {
                this.item = item;
                this.id.itemId = item.getId();
            }
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            OrderItemMapping that = (OrderItemMapping) o;
            return Objects.equals(id, that.id) &&
                    Objects.equals(order, that.order);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id, order);
        }
    }



    @Entity
    @Table(name = "seller")
    public static class Seller {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id")
        private Long id;

        @Column(name = "name")
        private String name;

        public Seller() {
        }

        public Seller(String name) {
            this.name = name;
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

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Seller seller = (Seller) o;
            return id == seller.id &&
                    Objects.equals(name, seller.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id, name);
        }
    }

    @Entity
    @Table(name = "item")
    public static class Item {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(name = "id")
        private Long id;

        @Column(name = "name")
        private String name;

        public Item() {
        }

        public Item(String name) {
            this.name = name;
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

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Item item = (Item) o;
            return Objects.equals(id, item.id) &&
                    Objects.equals(name, item.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id, name);
        }
    }
}
