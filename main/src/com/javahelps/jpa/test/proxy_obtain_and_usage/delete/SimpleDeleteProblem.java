package com.javahelps.jpa.test.proxy_obtain_and_usage.delete;

import com.javahelps.jpa.test.model.Post;
import com.javahelps.jpa.test.model.PostComment;
import com.javahelps.jpa.test.util.PersistentHelper;

import javax.persistence.EntityManager;

public class SimpleDeleteProblem {
    public static void main(String[] args) {
        EntityManager entityManager = PersistentHelper.getEntityManager(new Class[] {Post.class, PostComment.class});

        saveData(entityManager);
        entityManager.clear();

        System.out.println();
        System.out.println("Before Post comment updated");
        System.out.println();

        entityManager.getTransaction().begin();

        Post post = entityManager.getReference(Post.class, 1L);
        entityManager.remove(post);

        entityManager.getTransaction().commit();

        System.out.println();
        System.out.println("After Post comment updated");
        System.out.println();
    }

    private static void saveData(EntityManager entityManager) {
        entityManager.getTransaction().begin();

        Post post = new Post("post 1");
        entityManager.persist(post);

        entityManager.getTransaction().commit();
    }
}
