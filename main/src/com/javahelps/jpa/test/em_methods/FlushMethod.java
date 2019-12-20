package com.javahelps.jpa.test.em_methods;

import com.javahelps.jpa.test.model.Post;
import com.javahelps.jpa.test.model.PostComment;
import com.javahelps.jpa.test.util.PersistentHelper;

import javax.persistence.EntityManager;

public class FlushMethod {
    public static void main(String[] args) {
        EntityManager entityManager = PersistentHelper.getEntityManager(new Class[] {Post.class, PostComment.class});

        //ожидаемое исполнение кода: в таблице будет сохранено 2 записи: Post 1 1 и Post 2 2; Однако: Post 1 и Post 2
        //так происходит потому что хибернейт откладывает по максимуму исполнение sql запросов. крайний момент - строчка
        //в которой мы комитим транзакцию. но если до этого мы очистили весь контекст персистентности методом clear
        //то хибернейту больше нечего обновлять, т.к. ни один объект не присоединен к сессии
        problem(entityManager);

        //решение этой проблемы простое - заставить хибернейт сбросить все данные перед тем, как вызвать метод clear
        solution(entityManager);
    }

    private static void problem(EntityManager entityManager) {
        entityManager.getTransaction().begin();

        Post post1 = new Post("Post 1");
        Post post2 = new Post("Post 2");

        entityManager.persist(post1);
        entityManager.persist(post2);

        post1.setTitle("Post 1 1");

        entityManager.detach(post1);

        post2.setTitle("Post 2 2");

        entityManager.clear();

        entityManager.getTransaction().commit();
    }

    private static void solution(EntityManager entityManager) {
        entityManager.getTransaction().begin();

        Post post1 = new Post("Post 1");
        Post post2 = new Post("Post 2");

        entityManager.persist(post1);
        entityManager.persist(post2);

        post1.setTitle("Post 1 1");

        entityManager.detach(post1);

        post2.setTitle("Post 2 2");

        entityManager.flush();  //

        entityManager.clear();

        entityManager.getTransaction().commit();
    }
}
