package com.javahelps.jpa.test.proxy_obtain_and_usage.update;

import com.javahelps.jpa.test.model.Post;
import com.javahelps.jpa.test.model.PostComment;
import com.javahelps.jpa.test.util.PersistentHelper;

import javax.persistence.EntityManager;

public class _1_Problem {
    public static void main(String[] args) {
        EntityManager entityManager = PersistentHelper.getEntityManager(new Class[] {Post.class, PostComment.class});

        saveData(entityManager);
        entityManager.clear();

        {

            System.out.println();
            System.out.println("Before post comment updated");
            System.out.println();

            entityManager.getTransaction().begin();

            Post post = entityManager.find(Post.class, 1L);

            PostComment postComment2 = entityManager.find(PostComment.class, 1L);

            post.addComment(postComment2);

            entityManager.getTransaction().commit();

            System.out.println();
            System.out.println("After post comment updated");
            System.out.println();
        }

        Post postFromCache = entityManager.find(Post.class, 1L);
        PostComment postCommentFromCachce = entityManager.find(PostComment.class, 1L);

        System.out.println(postFromCache.getComments());
        System.out.println(postCommentFromCachce.getPost());
        System.out.println();

        entityManager.clear();

        Post postFromDB = entityManager.find(Post.class, 1L);
        PostComment postCommentFromDB = entityManager.find(PostComment.class, 1L);

        System.out.println(postFromDB.getComments());
        System.out.println(postCommentFromDB.getPost());
    }

    private static void saveData(EntityManager entityManager) {
        entityManager.getTransaction().begin();

        Post post = new Post("post 1");

        PostComment postComment1 = new PostComment("post comment 1");
        PostComment postComment2 = new PostComment("post comment 2");

        entityManager.persist(post);

        post.addComment(postComment1);

        entityManager.persist(postComment2);

        entityManager.getTransaction().commit();
    }
}
