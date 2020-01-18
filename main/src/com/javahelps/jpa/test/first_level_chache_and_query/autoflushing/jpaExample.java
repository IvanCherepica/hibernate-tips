package com.javahelps.jpa.test.first_level_chache_and_query.autoflushing;

import com.javahelps.jpa.test.model.Post;
import com.javahelps.jpa.test.model.PostComment;
import com.javahelps.jpa.test.util.PersistentHelper;

import javax.persistence.EntityManager;

public class jpaExample {
    public static void main(String[] args) {
        EntityManager entityManager = PersistentHelper.getEntityManager(new Class[] {Post.class, PostComment.class});

        saveObjects(entityManager);
        entityManager.clear();

        hqlTest(entityManager);
        entityManager.clear();

        nativeTest(entityManager);
    }

    private static void hqlTest(EntityManager entityManager) {
        entityManager.getTransaction().begin();

        entityManager.createQuery("FROM Post p").getResultList();

        Post post = new Post("Post 2");
        entityManager.persist(post);

        post.setTitle("Post 3");

        entityManager.createQuery("FROM Post p").getResultList();

        entityManager.getTransaction().commit();
    }

    private static void nativeTest(EntityManager entityManager) {
        entityManager.getTransaction().begin();

        entityManager.createNativeQuery("SELECT * FROM post", Post.class).getResultList();

        Post post = new Post("Post 2");
        entityManager.persist(post);

        post.setTitle("Post 3");

        entityManager.createNativeQuery("SELECT * FROM post", Post.class).getResultList();

        entityManager.getTransaction().commit();
    }

    private static void saveObjects(EntityManager entityManager) {
        Post post = new Post("Post 1");
        PostComment postComment = new PostComment("Comment 1");

        entityManager.getTransaction().begin();

        //сохраняем объекты
        entityManager.persist(post);
        post.addComment(postComment);

        entityManager.getTransaction().commit();
    }
}
