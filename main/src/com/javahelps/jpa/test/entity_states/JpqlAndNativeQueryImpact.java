package com.javahelps.jpa.test.entity_states;

import com.javahelps.jpa.test.model.Post;
import com.javahelps.jpa.test.model.PostComment;
import com.javahelps.jpa.test.util.PersistentHelper;

import javax.persistence.EntityManager;

public class JpqlAndNativeQueryImpact {
    public static void main(String[] args) {
        EntityManager entityManager = PersistentHelper.getEntityManager(new Class[] {Post.class, PostComment.class});
        testUpdateQuery(entityManager);
    }

    private static void testUpdateQuery(EntityManager entityManager) {
        entityManager.getTransaction().begin();

        //создаём три объекта типа post в состоянии transient
        Post post1 = new Post("Post 1");
        Post post2 = new Post("Post 2");
        Post post3 = new Post("Post 3");

        //и переводим все в состояние persist
        entityManager.persist(post1);
        entityManager.persist(post2);
        entityManager.persist(post3);

        entityManager.createQuery("UPDATE Post p SET p.title = 'Post 1 1' WHERE p.id = 1").executeUpdate();

        entityManager.createNativeQuery("UPDATE post SET title = 'Post 2 2' WHERE id = 2").executeUpdate();

        entityManager.getTransaction().commit();

        //ни jpql ни нативный запрос не влияют на контекст персистентности. здесь мы теряем целостность данных
        System.out.println(post1.getTitle());
        System.out.println(post2.getTitle());
        System.out.println(post3.getTitle());
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
