package com.javahelps.jpa.test.one_to_many.bidirectional;

import com.javahelps.jpa.test.util.PersistentHelper;

import javax.persistence.*;
import javax.xml.stream.events.Comment;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class _7_Pattern {
    public static void main(String[] args) {
        EntityManager entityManager = PersistentHelper.getEntityManager(new Class[] {Post.class, PostComment.class});

        persistIssue(entityManager);
        removeIssue(entityManager);

        entityManager.getTransaction().begin();

        Post p = entityManager.find(Post.class, 1L);

        entityManager.getTransaction().commit();

        System.out.println(p);
    }

    private static void removeIssue(EntityManager entityManager) {
        entityManager.getTransaction().begin();

        System.out.println();
        System.out.println("Before removing");
        System.out.println();

        Post post = entityManager.find(Post.class, 1L);
        post.getPostComments().remove(0);

        entityManager.getTransaction().commit();

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

        PostComment postComment1 = new PostComment("Comment 1");
        PostComment postComment2 = new PostComment("Comment 2");
        PostComment postComment3 = new PostComment("Comment 3");
        PostComment postComment4 = new PostComment("Comment 4");
        PostComment postComment5 = new PostComment("Comment 5");

        entityManager.persist(post);

        post.addComment(postComment1);
        post.addComment(postComment2);
        post.addComment(postComment3);
        post.addComment(postComment4);
        post.addComment(postComment5);

        entityManager.getTransaction().commit();

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

        @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
        private List<PostComment> postComments = new ArrayList<>();

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

        public List<PostComment> getPostComments() {
            return postComments;
        }

        public void setPostComments(List<PostComment> postComments) {
            this.postComments = postComments;
        }

        public void addComment(PostComment postComment) {
            this.postComments.add(postComment);
            postComment.setPost(this);
        }

        public void removeComment(PostComment postComment) {
            this.postComments.remove(postComment);
            postComment.setPost(null);
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

        @ManyToOne
        private Post post;

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

        public Post getPost() {
            return post;
        }

        public void setPost(Post post) {
            this.post = post;
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
