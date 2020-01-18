package com.javahelps.jpa.test.first_level_chache_and_query;

import com.javahelps.jpa.test.model.Post;
import com.javahelps.jpa.test.model.PostComment;
import com.javahelps.jpa.test.util.PersistentHelper;

import javax.persistence.EntityManager;

//entitymanager=session
public class _1_Problem {
    public static void main(String[] args) {
        EntityManager entityManager = PersistentHelper.getEntityManager(new Class[] {Post.class, PostComment.class});

        saveObjects(entityManager);
        changeByObjectState(entityManager);
        changeByQuery(entityManager);
        changeByNativeQuery(entityManager);


        entityManager.getTransaction().begin();

        PostComment postComment = entityManager.find(PostComment.class, 1L);

        entityManager.getTransaction().commit();

        //каждый вызванный метод увеличивает счётчик в названии комента на 1
        //ожидаем, что будет Comment 4
        System.out.println(postComment);
        //однако видим Comment 2, потому что сущность была запрошена из кэша первого уровня
        //на данные в кэше первого уровня влияют только изменения, проведённые через состояние объекта
        //нативный запрос, или нет - разницы не существует
    }

    private static void changeByNativeQuery(EntityManager entityManager) {
        entityManager.getTransaction().begin();

        entityManager.createNativeQuery("update post_comment set review='Comment 4' where id=1").executeUpdate();

        entityManager.getTransaction().commit();
    }

    private static void changeByQuery(EntityManager entityManager) {
        entityManager.getTransaction().begin();

        entityManager.createQuery("UPDATE PostComment pc SET pc.review = 'Comment 3' WHERE pc.id = 1").executeUpdate();

        entityManager.getTransaction().commit();
    }

    private static void changeByObjectState(EntityManager entityManager) {
        entityManager.getTransaction().begin();

        PostComment postComment = entityManager.find(PostComment.class, 1L);

        postComment.setReview("Comment 2");

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
