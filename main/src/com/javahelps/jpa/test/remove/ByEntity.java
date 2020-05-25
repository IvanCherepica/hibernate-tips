package com.javahelps.jpa.test.remove;

import com.javahelps.jpa.test.util.PersistentHelper;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.List;

public class ByEntity {

    public static void main(String[] args) {
        EntityManager entityManager = PersistentHelper.getEntityManager(new Class[] {User.class, Address.class});
        saveData(entityManager);
        entityManager.clear();

        User user = null;

        {//загружаем сущность в контекс для последующей проверки синхронизации данных контекста и данных из бд
            entityManager.getTransaction().begin();
            System.out.println();
            System.out.println("load User with id: 1 into persistent context");
            user = entityManager.find(User.class, 1L);
            System.out.println();
            entityManager.getTransaction().commit();
        }

        {//удаляем только что загруженную сущность из базы (дополнительный select тут не выполняется из-за того, что ущнст язагружена выше)
            entityManager.getTransaction().begin();

            User userToRemove = entityManager.find(User.class, 1L);
            entityManager.remove(userToRemove);

            entityManager.getTransaction().commit();
        }

        {//пробуем ещё раз загрузить уже удаленную сущность и получаем null, т.к. контекст персистентности синхронизирован с бд
            //т.к. контекст синзронизировать - в нем больше нет данной сущности и хибернейт отправляет ещё один запрос в бд
            entityManager.getTransaction().begin();
            System.out.println();
            System.out.println("load User with id: 1 after deleting from db");
            System.out.println(entityManager.find(User.class, 1L));
            System.out.println();
            entityManager.getTransaction().commit();
        }

        {
            entityManager.getTransaction().begin();

            List<User> users = entityManager.createQuery("FROM "+User.class.getName(), User.class).getResultList();
            List<Address> addresses = entityManager.createQuery("FROM "+Address.class.getName(), Address.class).getResultList();

            users.forEach(System.out::println);
            addresses.forEach(System.out::println);

            entityManager.getTransaction().commit();
        }
    }

    private static void saveData(EntityManager entityManager) {
        entityManager.getTransaction().begin();

        User user1 = new User("1");
        User user2 = new User("2");
        User user3 = new User("3");

        entityManager.persist(user1);
        entityManager.persist(user2);
        entityManager.persist(user3);

        Address address1 = new Address("street 1", 1);
        Address address2 = new Address("street 2", 2);
        Address address3 = new Address("street 3", 3);

        address1.setUser(user1);
        address2.setUser(user2);
        address3.setUser(user3);

        entityManager.persist(address1);
        entityManager.persist(address2);
        entityManager.persist(address3);

        entityManager.getTransaction().commit();
    }


    @Entity
    static class User {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String password;

        public User() {
        }

        public User(String password) {
            this.password = password;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        @Override
        public String toString() {
            return "User{" +
                    "id=" + id +
                    ", password='" + password + '\'' +
                    '}';
        }
    }

    @Entity
    static class Address {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String street;

        private Integer index;

        @OneToOne(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
        @OnDelete(action = OnDeleteAction.CASCADE)
        @JoinColumn(name = "user_id")
        private User user;

        public Address() {
        }

        public Address(String street, Integer index) {
            this.street = street;
            this.index = index;
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

        public Integer getIndex() {
            return index;
        }

        public void setIndex(Integer index) {
            this.index = index;
        }

        public User getUser() {
            return user;
        }

        public void setUser(User user) {
            this.user = user;
        }

        @Override
        public String toString() {
            return "Address{" +
                    "id=" + id +
                    ", street='" + street + '\'' +
                    ", index=" + index +
                    '}';
        }
    }
}
