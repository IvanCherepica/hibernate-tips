package com.javahelps.jpa.test.entity_graph_example;

import com.javahelps.jpa.test.util.PersistentHelper;

import javax.persistence.*;
import java.util.*;

public class DynamicSubGraphExample {
    public static void main(String[] args) {
        EntityManager entityManager = PersistentHelper.getEntityManager(new Class[] {Order.class, OrderItem.class, Product.class});

        saveData(entityManager);
        entityManager.clear();

        {//в нашем примере используется ленивая загрузка, что бы в случаях, когда требуется загрузить только заказ - ничего лишнего
            //не доставалось из базы, как в этом случае

            System.out.println();
            System.out.println("Before select without graph");
            System.out.println();

            entityManager.getTransaction().begin();

            Order order = entityManager.find(Order.class, 1L);

            entityManager.getTransaction().commit();

            System.out.println();
            System.out.println("After select without graph");
            System.out.println();
        }

        entityManager.clear();

        {//используя метод addSubgraph мы просим хибернейт выгрузить все свяаннаые с item product
            entityManager.getTransaction().begin();

            System.out.println();
            System.out.println("Before select with graph");
            System.out.println();

            EntityGraph<Order> entityGraph = entityManager.createEntityGraph(Order.class);
            Subgraph<OrderItem> itemGraph = entityGraph.addSubgraph("items");
            itemGraph.addAttributeNodes("product");

            Map<String, Object> hints = new HashMap<>();
            hints.put("javax.persistence.loadgraph", entityGraph);

            Order order = entityManager.find(Order.class, 1L, hints);

            entityManager.getTransaction().commit();

            System.out.println();
            System.out.println("After select with graph");
            System.out.println();
        }

        entityManager.clear();


        {//используя метод addSubgraph мы просим хибернейт выгрузить все свяаннаые с item product
            entityManager.getTransaction().begin();

            System.out.println();
            System.out.println("Before query select with graph");
            System.out.println();

            EntityGraph<Order> entityGraph = entityManager.createEntityGraph(Order.class);
            Subgraph<OrderItem> itemGraph = entityGraph.addSubgraph("items");
            itemGraph.addAttributeNodes("product");

            List<Order> order = entityManager.createQuery("FROM " + Order.class.getName(), Order.class)
                    .setHint("javax.persistence.loadgraph", entityGraph)
                    .getResultList();

            entityManager.getTransaction().commit();

            System.out.println();
            System.out.println("After query select with graph");
            System.out.println();

            System.out.println(order.get(0).getItems());
        }

        entityManager.clear();

        {//ничего из этого не сработает на нативных запросах
            entityManager.getTransaction().begin();

            System.out.println();
            System.out.println("Before query select with graph");
            System.out.println();

            EntityGraph<Order> entityGraph = entityManager.createEntityGraph(Order.class);
            Subgraph<OrderItem> itemGraph = entityGraph.addSubgraph("items");
            itemGraph.addAttributeNodes("product");

            List<Order> order = entityManager.createNativeQuery("SELECT * FROM purchaseOrder", Order.class)
                    .getResultList();

            entityManager.getTransaction().commit();

            System.out.println();
            System.out.println("After query select with graph");
            System.out.println();

            System.out.println(order.get(0).getItems());
        }


    }

    private static void saveData(EntityManager entityManager) {
        entityManager.getTransaction().begin();

        Order order = new Order();
        Product product = new Product();

        OrderItem orderItem1 = new OrderItem();
        OrderItem orderItem2 = new OrderItem();
        OrderItem orderItem3 = new OrderItem();

        entityManager.persist(order);
        entityManager.persist(product);
        entityManager.persist(orderItem1);
        entityManager.persist(orderItem2);
        entityManager.persist(orderItem3);

        orderItem1.setOrder(order);
        orderItem2.setOrder(order);
        orderItem3.setOrder(order);

        orderItem1.setProduct(product);
        orderItem2.setProduct(product);
        orderItem3.setProduct(product);

        Set<OrderItem> orderItems = new HashSet<>();
        orderItems.add(orderItem1);
        orderItems.add(orderItem2);
        orderItems.add(orderItem3);

        entityManager.getTransaction().commit();

    }

    @Entity
    @Table(name = "purchaseOrder")
    private static class Order {

        @Id
        @GeneratedValue
        private Long id;

        @Version
        private int version;

        private String orderNumber;

        @OneToMany(mappedBy = "order", fetch = FetchType.LAZY)
        private Set<OrderItem> items = new HashSet<>();

        public Order() {
        }

        public Order(int version, String orderNumber, Set<OrderItem> items) {
            this.version = version;
            this.orderNumber = orderNumber;
            this.items = items;
        }


        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public int getVersion() {
            return version;
        }

        public void setVersion(int version) {
            this.version = version;
        }

        public String getOrderNumber() {
            return orderNumber;
        }

        public void setOrderNumber(String orderNumber) {
            this.orderNumber = orderNumber;
        }

        public Set<OrderItem> getItems() {
            return items;
        }

        public void setItems(Set<OrderItem> items) {
            this.items = items;
        }
    }

    @Entity
    private static class OrderItem {

        @Id
        @GeneratedValue
        private Long id;

        @Version
        private int version;

        private int quantity;

        @ManyToOne
        private Order order;

        @ManyToOne(fetch = FetchType.LAZY)
        private Product product;

        public OrderItem() {
        }

        public OrderItem(int version, int quantity, Order order, Product product) {
            this.version = version;
            this.quantity = quantity;
            this.order = order;
            this.product = product;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public int getVersion() {
            return version;
        }

        public void setVersion(int version) {
            this.version = version;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public Order getOrder() {
            return order;
        }

        public void setOrder(Order order) {
            this.order = order;
        }

        public Product getProduct() {
            return product;
        }

        public void setProduct(Product product) {
            this.product = product;
        }
    }

    @Entity
    private static class Product {

        @Id
        @GeneratedValue
        private Long id;

        @Version
        private int version;

        private String name;

        public Product() {
        }

        public Product(int version, String name) {
            this.version = version;
            this.name = name;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public int getVersion() {
            return version;
        }

        public void setVersion(int version) {
            this.version = version;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
