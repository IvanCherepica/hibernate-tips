package com.javahelps.jpa.test.entity_states;

import com.javahelps.jpa.test.model.Post;
import com.javahelps.jpa.test.model.PostComment;
import com.javahelps.jpa.test.util.PersistentHelper;

import javax.persistence.EntityManager;
import javax.swing.text.html.parser.Entity;

public class DetachedOnly {
    public static void main(String[] args) {
        EntityManager entityManager = PersistentHelper.getEntityManager(new Class[] {Post.class, PostComment.class});
        testClear(entityManager);
    }

    private static void testDetach(EntityManager entityManager) {
        entityManager.getTransaction().begin();

        //создаём три объекта типа post в состоянии transient
        Post post1 = new Post("Post 1");
        Post post2 = new Post("Post 2");
        Post post3 = new Post("Post 3");

        //и переводим все в состояние persist
        entityManager.persist(post1);
        entityManager.persist(post2);
        entityManager.persist(post3);

        //явно отсоединяем post1 от контекста персистентности
        entityManager.detach(post1);

        //изменяем состояние всех трёх объектов; по базе видим, что изменилось сотояние только 2 и 3 записи в таблице
        post1.setTitle("Post 1 1");
        post2.setTitle("Post 2 2");
        post3.setTitle("Post 3 3");

        entityManager.getTransaction().commit();
    }

    private static void testClear(EntityManager entityManager) {
        entityManager.getTransaction().begin();

        //создаём три объекта типа post в состоянии transient
        Post post1 = new Post("Post 1");
        Post post2 = new Post("Post 2");
        Post post3 = new Post("Post 3");

        //и переводим все в состояние persist
        entityManager.persist(post1);
        entityManager.persist(post2);
        entityManager.persist(post3);

        //очищаем полностью весь контекст персистентности
        entityManager.clear();

        //т.к. все объекты сейчас находятся в состоянии detached - никаких изменений в базе не будет
        post1.setTitle("Post 1 1");
        post2.setTitle("Post 2 2");
        post3.setTitle("Post 3 3");

        entityManager.getTransaction().commit();
    }
}
