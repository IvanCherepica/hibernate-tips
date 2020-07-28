package com.javahelps.jpa.test.em_methods;

import com.javahelps.jpa.test.model.Post;
import com.javahelps.jpa.test.model.PostComment;
import com.javahelps.jpa.test.util.PersistentHelper;

import javax.persistence.EntityManager;

public class MergeMethod {
    public static void main(String[] args) {
        EntityManager entityManager = PersistentHelper.getEntityManager(new Class[] {Post.class, PostComment.class});

        entityManager.getTransaction().begin();
        Post post = new Post("Post 1");
        entityManager.persist(post);
        entityManager.getTransaction().commit();

        entityManager.close();

        entityManager = PersistentHelper.getEntityManager(new Class[] {Post.class, PostComment.class});

        entityManager.getTransaction().begin();
        Post post1 = entityManager.find(Post.class, post.getId());
        post1.setTitle("Post 2");
        entityManager.merge(post);  //изменения из post1 не попадут в бд
        entityManager.getTransaction().commit();
    }
}
