package com.javahelps.jpa.test.entity_states;

import com.javahelps.jpa.test.model.Post;
import com.javahelps.jpa.test.model.PostComment;
import com.javahelps.jpa.test.util.PersistentHelper;

import javax.persistence.EntityManager;

public class PersistentAndDetached {
    public static void main(String[] args) {
        EntityManager entityManager = PersistentHelper.getEntityManager(new Class[] {Post.class, PostComment.class});

        entityManager.getTransaction().begin();

        //Объект в состоянии transient или new. Этот объект не привязан к сессии он не ассоциирован ни с одной записью в бд
        Post post = new Post("Post 1");

        //Теперь объект в состоянии persistent. Контекст персистентности следит за состоянием объекта
        //В таблице появляется запись, связанная с этим объектом
        entityManager.persist(post);

        //Если изменить состояние объекта, который находится в состояние persistent - изменится и запись в таблице
        post.setTitle("Post 2");

        //закрываем тразакци, и связанную с ней сессию; иными словами - закрываем текущий контекст персистентности
        //все сущности, которые были раньше привязаны к этому контексту переходят в состояние Detached
        entityManager.getTransaction().commit();

        //т.к. объект post больше не привязан к конткесту и находится в состоянии detached - изменение его состояния
        //не будет проецироваться на базу
        post.setTitle("Post 3");

        //если же закрытие контекста персистентности перенести ниже - то название в бд будет Post 3
        //entityManager.getTransaction().commit();

        //что делаеть, если нужно обновить состояние обхекта? всё просто - надо использовать метод merge
        entityManager.getTransaction().begin();
        //merge используется только для detached объектов
        entityManager.merge(post);

        entityManager.getTransaction().commit();
    }
}
