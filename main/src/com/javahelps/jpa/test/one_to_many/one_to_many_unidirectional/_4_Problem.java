package com.javahelps.jpa.test.one_to_many.one_to_many_unidirectional;

import com.javahelps.jpa.test.util.PersistentHelper;
import javafx.geometry.Pos;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class _4_Problem {
    public static void main(String[] args) {
        EntityManager entityManager = PersistentHelper.getEntityManager(new Class[] {Post.class, PostComment.class});

        //при сохранении в бд происходит сначала вставка в таблицу post, потом в post_comment, а затем делается
        //update post_comment set post_id=? where id=?
        //т.е. вместо двух запросов мы получаем 3
        saveData(entityManager);
        entityManager.clear();
        System.out.println();
        System.out.println("--после вставки данных--");
        System.out.println();

        System.out.println();
        System.out.println("Обычное удаление элемента");
        System.out.println();

        entityManager.getTransaction().begin();

        PostComment postComment = entityManager.find(PostComment.class, 2L);
        entityManager.remove(postComment);


        //Всё ок, получаем 1 запрос на удаление
        entityManager.getTransaction().commit();


        System.out.println();
        System.out.println("Удаление через коллекцию");
        System.out.println();

        entityManager.getTransaction().begin();

        Post post = entityManager.find(Post.class, 1L);
        post.getComments().remove(0);

        //перед удалением записи из таблицы post_comment происходит update post_comment set post_id=null where post_id=? and id=?
        //вместо 1 запроса мы получаем 2
        entityManager.getTransaction().commit();

    }

    private static void saveData(EntityManager entityManager) {
        entityManager.getTransaction().begin();

        Post post1 = new Post("Post 1");
        Post post2 = new Post("Post 2");

        post1.getComments().add(new PostComment("Comment 1"));
        post1.getComments().add(new PostComment("Comment 2"));
        post1.getComments().add(new PostComment());

        post2.getComments().add(new PostComment("Comment 4"));
        post2.getComments().add(new PostComment("Comment 5"));
        post2.getComments().add(new PostComment());

        entityManager.persist(post1);
        entityManager.persist(post2);

        entityManager.getTransaction().commit();
    }

    @Entity(name = "Post")
    @Table(name = "post")
    public static class Post {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String title;

        @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
//        @JoinColumn(name = "post_id")
//        @OneToMany
        @JoinColumn(name = "post_id")
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

//        @ManyToOne(fetch = FetchType.LAZY)
        private PostComment postComment;

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
