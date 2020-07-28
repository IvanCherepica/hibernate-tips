package com.javahelps.jpa.test.em_methods;

import com.javahelps.jpa.test.model.Post;
import com.javahelps.jpa.test.model.PostComment;
import com.javahelps.jpa.test.util.PersistentHelper;

import javax.persistence.EntityManager;

public class RefreshMethod {
    public static void main(String[] args) {
        EntityManager entityManager = PersistentHelper.getEntityManager(new Class[] {Post.class, PostComment.class});

        entityManager.getTransaction().begin();
        Post post = new Post("Post 1");
        entityManager.persist(post);

        entityManager.createQuery("UPDATE Post p SET p.title = 'Post 2' WHERE p.id = :id")
                .setParameter("id", post.getId())
                .executeUpdate();

        entityManager.getTransaction().commit();

        System.out.println("Post title before refresh: " + post.getTitle());

        entityManager.refresh(post);

        System.out.println("Post title after refresh: " + post.getTitle());

        entityManager.close();

    }
}
