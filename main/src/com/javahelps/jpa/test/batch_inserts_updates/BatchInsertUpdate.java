package com.javahelps.jpa.test.batch_inserts_updates;

import com.javahelps.jpa.test.util.PersistentHelper;
import org.hibernate.Session;
import org.hibernate.stat.Statistics;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class BatchInsertUpdate {

    private static EntityManager entityManager;

    public static void main(String[] args) {
        entityManager = PersistentHelper.getEntityManager(new Class[] {Post.class});

        saveData();
    }

    private static void saveData() {

        entityManager.getTransaction().begin();

        Post post1 = new Post("post 1");
        Post post2 = new Post("post 2");

        entityManager.persist(post1);
        entityManager.persist(post2);

        entityManager.getTransaction().commit();



        entityManager.getTransaction().begin();

        post1.setTitle("post 11");
        post2.setTitle("post 22");

        entityManager.merge(post1);
        entityManager.merge(post2);

        entityManager.getTransaction().commit();

        Session session = (Session) entityManager.getDelegate();
        Statistics statistic = session.getSessionFactory().getStatistics();
        System.out.println("INSERT:" + statistic.getEntityInsertCount());
        System.out.println("UPDATE: " + statistic.getEntityUpdateCount());
    }

    @Entity(name = "Post")
    @Table(name = "post")
    private static class Post {

        @Id
        @GeneratedValue(strategy = GenerationType.SEQUENCE)
        private Long id;

        private String title;
        
        public Post() {}

        public Post(String title) {
            this.title = title;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}
