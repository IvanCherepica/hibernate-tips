package com.javahelps.jpa.test.cascade.listeners;

import com.javahelps.jpa.test.cascade.listeners.model.Post;
import com.javahelps.jpa.test.cascade.listeners.model.PostComment;
import com.javahelps.jpa.test.cascade.listeners.model.RelatedTheme;
import com.javahelps.jpa.test.cascade.ondelete.DeleteCascadeProblem_1;
import com.javahelps.jpa.test.util.PersistentHelper;

import javax.persistence.EntityManager;

public class Main {

    public static void main(String[] args) {
        EntityManager entityManager = PersistentHelper.getEntityManager(new Class[] {Post.class, PostComment.class, RelatedTheme.class});
        saveData(entityManager);

        entityManager.clear();

        entityManager.getTransaction().begin();

        Post post1 = entityManager.find(Post.class, 1L);
        entityManager.remove(post1);

        entityManager.getTransaction().commit();
    }


    public static void saveData(EntityManager entityManager) {
        entityManager.getTransaction().begin();

        Post post1 = new Post("Post 1");
        Post post2 = new Post("Post 2");

        entityManager.persist(post1);
        entityManager.persist(post2);

        RelatedTheme relatedTheme1 = new RelatedTheme("Theme 1");
        RelatedTheme relatedTheme2 = new RelatedTheme("Theme 2");
        RelatedTheme relatedTheme3 = new RelatedTheme("Theme 3");
        RelatedTheme relatedTheme4 = new RelatedTheme("Theme 4");

        entityManager.persist(relatedTheme1);
        entityManager.persist(relatedTheme2);
        entityManager.persist(relatedTheme3);
        entityManager.persist(relatedTheme4);

        PostComment postComment1 = new PostComment("Comment 1");
        PostComment postComment2 = new PostComment("Comment 2");
        PostComment postComment3 = new PostComment("Comment 3");
        PostComment postComment4 = new PostComment("Comment 4");

        postComment1.setPost(post1);
        postComment1.setRelatedTheme(relatedTheme1);
        postComment2.setPost(post1);
        postComment2.setRelatedTheme(relatedTheme2);
        postComment3.setPost(post2);
        postComment3.setRelatedTheme(relatedTheme3);
        postComment4.setPost(post2);
        postComment4.setRelatedTheme(relatedTheme4);

        entityManager.persist(postComment1);
        entityManager.persist(postComment2);
        entityManager.persist(postComment3);
        entityManager.persist(postComment4);

        entityManager.getTransaction().commit();
    }
}
