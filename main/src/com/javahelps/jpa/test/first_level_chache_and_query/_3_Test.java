package com.javahelps.jpa.test.first_level_chache_and_query;

import com.javahelps.jpa.test.model.Post;
import com.javahelps.jpa.test.model.PostComment;
import com.javahelps.jpa.test.util.PersistentHelper;

import javax.persistence.EntityManager;

public class _3_Test {
    public static void main(String[] args) {
        EntityManager entityManager = PersistentHelper.getEntityManager(new Class[] {Post.class, PostComment.class});

        saveObjects(entityManager);

        //принудительно очищаем кэш первого уровня, что бы провреить - заругажет ли запрос в кэш объект
        entityManager.clear();

        //загружаем объект различными способами
//        loadByMethod(entityManager);
//        loadByQuery(entityManager);
        loadByNativeQuery(entityManager);

        System.out.println("Загрузка из main. Запрос ниже");
        entityManager.find(PostComment.class, 1L);

        //вывод: нет никакой разницы, между загрузкой через hql/sql. кешируется в любом случае
    }

    private static void loadByNativeQuery(EntityManager entityManager) {
        entityManager.getTransaction().begin();

        System.out.println("Загрузка через sql");
        entityManager.createNativeQuery("SELECT id, review, post_id FROM post_comment WHERE id = 1", PostComment.class).getSingleResult();

        entityManager.getTransaction().commit();
    }

    private static void loadByQuery(EntityManager entityManager) {
        entityManager.getTransaction().begin();

        System.out.println("Загрузка через hql");
        entityManager.createQuery("SELECT pc FROM PostComment pc WHERE pc.id = 1", PostComment.class).getSingleResult();

        entityManager.getTransaction().commit();
    }

    private static void loadByMethod(EntityManager entityManager) {
        entityManager.getTransaction().begin();

        System.out.println("Загрузка через метод");
        entityManager.find(PostComment.class, 1L);

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
