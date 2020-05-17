package com.javahelps.jpa.test.cascade.listeners;

import com.javahelps.jpa.test.cascade.listeners.model.Post;
import com.javahelps.jpa.test.cascade.listeners.model.PostComment;
import com.javahelps.jpa.test.cascade.listeners.model.RelatedTheme;
import com.javahelps.jpa.test.cascade.ondelete.DeleteCascadeProblem_1;
import com.javahelps.jpa.test.util.PersistentHelper;

import javax.persistence.EntityManager;
import javax.persistence.PreRemove;
import java.util.List;

public class PostListener {

    @PreRemove
    private void preRemove(Post post) {
        EntityManager entityManager = PersistentHelper.getEntityManager(new Class[] {Post.class, PostComment.class, RelatedTheme.class});

        entityManager.getTransaction().begin();

        List<PostComment> postComments = entityManager
                .createQuery("FROM " +PostComment.class.getSimpleName()+" pc WHERE pc.post.id = :postId", PostComment.class)
                .setParameter("postId", post.getId())
                .getResultList();

        for (PostComment postComment : postComments) {
            entityManager.remove(postComment);
        }

        entityManager.getTransaction().commit();
    }
}
