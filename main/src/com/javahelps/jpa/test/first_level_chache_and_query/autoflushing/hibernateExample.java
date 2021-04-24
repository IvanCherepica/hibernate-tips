package com.javahelps.jpa.test.first_level_chache_and_query.autoflushing;

import com.javahelps.jpa.test.model.Post;
import com.javahelps.jpa.test.model.PostComment;
import com.javahelps.jpa.test.util.PersistentHelper;
import org.hibernate.Session;

import javax.persistence.EntityManager;

public class hibernateExample {
    public static void main(String[] args) {
        EntityManager entityManager = PersistentHelper.getEntityManager(new Class[] {Post.class, PostComment.class});

        Session session = (Session) entityManager.getDelegate();
        session.beginTransaction();

        saveObjects(entityManager);
        entityManager.clear();

        hqlTest(session);
        entityManager.clear();

        nativeTest(session);
    }

    private static void hqlTest(Session session) {
        session.beginTransaction();

        session.createQuery("FROM Post p").getResultList();

        Post post = new Post("Post 2");
        session.save(post);

        post.setTitle("Post 3");

        session.createQuery("FROM Post p").getResultList();

        session.getTransaction().commit();
    }

    private static void nativeTest(Session session) {
        session.getTransaction().begin();

        session.createNativeQuery("SELECT * FROM post", Post.class).getResultList();

        Post post = new Post("Post 2");
        session.save(post);

        post.setTitle("Post 3");

        session.createNativeQuery("SELECT * FROM post", Post.class).getResultList();

        session.getTransaction().commit();
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
