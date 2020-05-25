package com.javahelps.jpa.test.test_context;

import com.javahelps.jpa.test.util.PersistentHelper;
import org.hibernate.Session;

import javax.persistence.*;
import java.util.List;

public class Test1 {

    private static EntityManager entityManager;

    public static void main(String[] args) {
        entityManager = PersistentHelper.getEntityManager(new Class[] {User.class, Address.class});

        saveData();

        test1();
//        test2();
//        test3();
    }

    private static void test1() {

        User user = entityManager.find(User.class, 1L);
        user.setLogin("login 2");

        entityManager.merge(user);

        String currentLogin = (String) entityManager.createQuery(
                "SELECT u.login FROM "+User.class.getName()+" u WHERE u.id=:id"
        )
                .setParameter("id", user.getId())
                .getSingleResult();

        System.out.println(currentLogin);
    }

    private static void test2() {
        entityManager.getTransaction().begin();

        User user = entityManager.find(User.class, 1L);
        entityManager.remove(user);

        entityManager.getTransaction().commit();
    }

    private static void test3() {
        entityManager.getTransaction().begin();

        User user = entityManager.find(User.class, 1L);

        entityManager.createQuery("DELETE FROM "+Address.class.getName()+" a WHERE a.user.id=:id")
                .setParameter("id", user.getId())
                .executeUpdate();

        entityManager.flush();
        entityManager.getTransaction().commit();

        Address address1 = entityManager.find(Address.class, 1L);

        System.out.println(address1.getId() + " " + address1.getStreet());


    }

    private static void saveData() {
        entityManager.getTransaction().begin();

        User user = new User("login 1");
        Address address = new Address("street 1");
        address.setUser(user);

        entityManager.persist(user);
        entityManager.persist(address);

        entityManager.getTransaction().commit();
    }

    private static boolean isAddressWithThatUserExist(Long userId) {
        long count = (long) entityManager.createQuery(
                "SELECT COUNT(a.id) FROM " + Address.class.getName() + " a WHERE a.user.id = :userId"
        )
                .setParameter("userId", userId)
                .getSingleResult();
        return count > 0;
    }

    private static boolean isUserWithThatLoginExist(String login) {

        long count = (long) entityManager.createQuery(
                "SELECT COUNT(u.id) FROM " + User.class.getName() + " u WHERE u.login = :login"
        )
                .setParameter("login", login)
                .getSingleResult();
        return count > 0;
    }

    @Entity
    @Table(name = "user")
    static class User {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String login;

        public User() {
        }

        public User(String login) {
            this.login = login;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getLogin() {
            return login;
        }

        public void setLogin(String login) {
            this.login = login;
        }

    }

    @Entity
    @Table(name = "address")
    static class Address {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String street;

        @OneToOne(cascade = CascadeType.REMOVE, fetch = FetchType.EAGER, optional = false)
        @JoinColumn(name = "user_id")
        private User user;

        public Address() {
        }

        public Address(String street) {
            this.street = street;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getStreet() {
            return street;
        }

        public void setStreet(String street) {
            this.street = street;
        }

        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }
    }
}
