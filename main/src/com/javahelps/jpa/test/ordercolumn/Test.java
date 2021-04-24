package com.javahelps.jpa.test.ordercolumn;

import com.javahelps.jpa.test.basic.BasicTest;
import com.javahelps.jpa.test.util.PersistentHelper;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderColumn;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;

public class Test {

    public static void main(String[] args) {
        EntityManager entityManager = PersistentHelper.getEntityManager(new Class[] {Post.class, Comment.class});
        saveData(entityManager);
        entityManager.clear();

//        deleteExample(entityManager);
//        updateExample(entityManager);
        changePosition(entityManager, 0, 2);

        System.out.println();
    }

    private static void changePosition(EntityManager entityManager, int fromIndex, int toIndex) {
        entityManager.getTransaction().begin();

        Post post = entityManager
                .createQuery(
                        "SELECT p FROM " + Post.class.getName() + " p JOIN FETCH p.comments WHERE p.id = :postId",
                        Post.class
                )
                .setParameter("postId", 1L)
                .getSingleResult();

        Comment comment = post.getComments().get(fromIndex);

        post.getComments().remove(fromIndex);
        post.getComments().add(toIndex, comment);

        entityManager.getTransaction().commit();
    }

    //не отработает, из-за updatable=false
    private static void updateExample(EntityManager entityManager) {
        entityManager.getTransaction().begin();

        Comment comment = entityManager.find(Comment.class, 1L);
        comment.setPosition(10);

        entityManager.getTransaction().commit();
    }

    private static void deleteExample(EntityManager entityManager) {
        entityManager.getTransaction().begin();

        Post post = entityManager.find(Post.class, 1L);
        Comment comment = entityManager.find(Comment.class, 1L);

        post.removeComment(comment);

        entityManager.getTransaction().commit();
    }

    private static void saveData(EntityManager entityManager) {
        entityManager.getTransaction().begin();

        Post post = new Post();
        post.setContent("new Post");

        Comment comment1 = new Comment("comment 1");
        Comment comment2 = new Comment("comment 2");
        Comment comment3 = new Comment("comment 3");

        post.addComment(comment1);
        post.addComment(comment2);
        post.addComment(comment3);

        entityManager.persist(post);

        entityManager.getTransaction().commit();
    }

    @Entity
    @NoArgsConstructor
    @Getter
    @Setter
    @Table(name = "post")
    static class Post {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String content;

        @OneToMany(mappedBy = "post", cascade = CascadeType.PERSIST, orphanRemoval = true)
        @OrderColumn(name = "position")
        private List<Comment> comments = new ArrayList<>();

        public void addComment(Comment comment) {
            this.comments.add(comment);
            comment.setPost(this);
        }

        public void removeComment(Comment comment) {
            comments.remove(comment);
            comment.setPost(null);
        }
    }

    @Entity
    @NoArgsConstructor
    @Getter
    @Setter
    @Table(name = "comment")
    static class Comment {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String content;

        @Column(updatable = false, insertable = false)
        private Integer position;

        @ManyToOne
        private Post post;

        public Comment(String content) {
            this.content = content;
        }
    }
}
