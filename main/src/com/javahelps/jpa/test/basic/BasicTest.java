package com.javahelps.jpa.test.basic;

import com.javahelps.jpa.test.inheritance.Joined;
import com.javahelps.jpa.test.util.PersistentHelper;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.SecondaryTable;
import javax.persistence.Table;

public class BasicTest {

    public static void main(String[] args) {
        EntityManager entityManager = PersistentHelper.getEntityManager(new Class[] {Post.class});
        saveData(entityManager);
        entityManager.clear();

        entityManager.getTransaction().begin();

        Post post = entityManager.find(Post.class, 1L);

        entityManager.getTransaction().commit();

        System.out.println();
    }

    private static void saveData(EntityManager entityManager) {
        entityManager.getTransaction().begin();

        Post post = new Post();
//        post.setText("... очень длинный текст ...");
//        post.setDescription("Описание");

        entityManager.persist(post);

        entityManager.getTransaction().commit();
    }

    @Entity
    @NoArgsConstructor
    @Getter
    @Setter
    static class Post {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Lob
        @Basic(fetch = FetchType.LAZY)
        private byte[] content;

        private String description;
    }
}
