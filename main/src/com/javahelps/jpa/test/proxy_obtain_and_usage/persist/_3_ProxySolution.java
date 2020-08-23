package com.javahelps.jpa.test.proxy_obtain_and_usage.persist;

import com.javahelps.jpa.test.model.Post;
import com.javahelps.jpa.test.model.PostComment;
import com.javahelps.jpa.test.util.PersistentHelper;

import javax.persistence.EntityManager;

public class _3_ProxySolution {
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

            //не обращаемся в базу - просто получаем прокси, за которой следит хибернейт
            Post post = entityManager.getReference(Post.class, 1L);

            postComment2.setPost(post);
            //в косноли вывод "PostComment pre persist", значит все жизненные циклы этой сущности задействуются
            //(например, каскады)
            entityManager.persist(postComment2);

            entityManager.getTransaction().commit();

            System.out.println();
            System.out.println("After post comment persist");
            System.out.println();
        }

        //не обращаемся в бд, т.к. до этого работали с объектами и PostComment был добавлен в кэш
        PostComment postCommentFromCachce = entityManager.find(PostComment.class, 2L);

        //но за Post идет обращение в бд, т.к. до этого мы имели только прокси, но не саму сущность
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
