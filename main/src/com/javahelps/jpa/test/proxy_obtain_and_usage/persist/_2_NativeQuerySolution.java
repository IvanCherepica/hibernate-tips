package com.javahelps.jpa.test.proxy_obtain_and_usage.persist;

import com.javahelps.jpa.test.model.Post;
import com.javahelps.jpa.test.model.PostComment;
import com.javahelps.jpa.test.util.PersistentHelper;

import javax.persistence.EntityManager;

public class _2_NativeQuerySolution {
    public static void main(String[] args) {
        EntityManager entityManager = PersistentHelper.getEntityManager(new Class[] {Post.class, PostComment.class});

        saveData(entityManager);
        entityManager.clear();

        {

            System.out.println();
            System.out.println("Before post comment persist");
            System.out.println();

            entityManager.getTransaction().begin();

            //что бы избежать дополнительного запроса на выборку из таблицы post - делаем сразу вставку в post_comment
            entityManager.createNativeQuery("INSERT INTO post_comment (post_id, review) values (1, 'post comment 2')").executeUpdate();
            //в консоли нет ни одной записи "PostComment pre persist", значит жизненные циклы сущности не задействуются
            //каскады не работают, проверки в Listeners тоже
            entityManager.getTransaction().commit();

            System.out.println();
            System.out.println("After post comment persist");
            System.out.println();
        }

        //однако, если теперь мы попытаемся достать только что сохраненный PostComment - отправится запрос, т.к. такого объекта нет в кэше
        PostComment postCommentFromCachce = entityManager.find(PostComment.class, 2L);
        //так же достается post
        System.out.println(postCommentFromCachce.getPost());
        System.out.println();
    }

    private static void saveData(EntityManager entityManager) {
        entityManager.getTransaction().begin();

        Post post = new Post("post 1");

        PostComment postComment1 = new PostComment("post comment 1");

        entityManager.persist(post);

        post.addComment(postComment1);

        entityManager.getTransaction().commit();
    }
}
