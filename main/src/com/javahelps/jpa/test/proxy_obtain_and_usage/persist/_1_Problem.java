package com.javahelps.jpa.test.proxy_obtain_and_usage.persist;

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
            System.out.println("Before post comment persist");
            System.out.println();

            entityManager.getTransaction().begin();

            PostComment postComment2 = new PostComment("post comment 2");

            //для присоединения к существующей сущности используем find и отправляем select запрос в бд
            Post post = entityManager.find(Post.class, 1L);

            postComment2.setPost(post);
            //в косноли вывод "PostComment pre persist", значит все жизненные циклы этой сущности задействуются
            //(например, каскады)
            entityManager.persist(postComment2);

            entityManager.getTransaction().commit();

            System.out.println();
            System.out.println("After post comment persist");
            System.out.println();
        }

        //в кэш был сохранен PostComment с id=2, так что необходимость в запросе отпадает
        PostComment postCommentFromCachce = entityManager.find(PostComment.class, 2L);
        //так же дополнительного запроса не будет и в этом случае, т.к. post был заранее выбран
        //однако, уверенннсти в том, что выбранная сущность бует использована позже есть не всегда
        //поэтому выборку из табицы post можно считать избыточной
        System.out.println(postCommentFromCachce.getPost());
        System.out.println();

        entityManager.clear();

        //запрос отправится только в случае сброса кэша
        PostComment postCommentFromDB = entityManager.find(PostComment.class, 2L);

        System.out.println(postCommentFromDB.getPost());
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
