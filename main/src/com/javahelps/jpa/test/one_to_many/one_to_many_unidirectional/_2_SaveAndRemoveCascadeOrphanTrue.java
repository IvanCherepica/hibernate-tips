package com.javahelps.jpa.test.one_to_many.one_to_many_unidirectional;

import com.javahelps.jpa.test.util.PersistentHelper;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class _2_SaveAndRemoveCascadeOrphanTrue {
    public static void main(String[] args) {
        EntityManager entityManager = PersistentHelper.getEntityManager(new Class[] {Post.class, PostComment.class});

        persistIssue(entityManager);
        removeIssue(entityManager);
    }

    private static void removeIssue(EntityManager entityManager) {
        entityManager.getTransaction().begin();

        System.out.println();
        System.out.println("Before removing");
        System.out.println();

        Post post = entityManager.find(Post.class, 1L);

        post.getComments().remove(0);

        entityManager.getTransaction().commit();
        //вместо одного запроса на удаление - мы получаем 2 запроса на удаление и ещё 2 на вставку
        //хибернейт не может понять точно, какую запись из смежной таблицы надо удалить, поэтому
        //он удаляет вообще все связи, ассоциированные с этим постом
        //и затем вставляет новые связи (за место тех, которые были удалены, но которые должны остаться)

        System.out.println();
        System.out.println("After removing");
        System.out.println();
    }

    private static void persistIssue(EntityManager entityManager) {
        entityManager.getTransaction().begin();

        System.out.println();
        System.out.println("Before saving");
        System.out.println();

        Post post = new Post("Post 1");

        post.getComments().add(new PostComment("Comment 1"));
        post.getComments().add(new PostComment("Comment 2"));
        post.getComments().add(new PostComment("Comment 3"));
        post.getComments().add(new PostComment("Comment 4"));
        post.getComments().add(new PostComment("Comment 5"));

        entityManager.persist(post);

        entityManager.getTransaction().commit();
        //проблема в том, что на добавление 4 сущностей в базу тратится 7 запросов. Это происходит из-за того,
        //что на данный тип связи создается отдельная таблица, которую мы вынужденны поддерживать

        System.out.println();
        System.out.println("After saving");
        System.out.println();
    }

    @Entity(name = "Post")
    @Table(name = "post")
    public static class Post {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String title;

        @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
        private List<PostComment> comments = new ArrayList<>();

        public Post() {
        }

        public Post(String title) {
            this.title = title;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public List<PostComment> getComments() {
            return comments;
        }

        public void setComments(List<PostComment> comments) {
            this.comments = comments;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Post post = (Post) o;
            return Objects.equals(id, post.id) &&
                    Objects.equals(title, post.title);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id, title);
        }
    }

    @Entity(name = "PostComment")
    @Table(name = "post_comment")
    public static class PostComment {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String review;

        public PostComment() {
        }

        public PostComment(String review) {
            this.review = review;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getReview() {
            return review;
        }

        public void setReview(String review) {
            this.review = review;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            PostComment that = (PostComment) o;
            return Objects.equals(id, that.id) &&
                    Objects.equals(review, that.review);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id, review);
        }
    }
}
