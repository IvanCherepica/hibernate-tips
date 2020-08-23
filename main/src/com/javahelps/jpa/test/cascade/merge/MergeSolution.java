package com.javahelps.jpa.test.cascade.merge;

import com.javahelps.jpa.test.util.PersistentHelper;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

public class MergeSolution {
    public static void main(String[] args) {
        EntityManager entityManager = PersistentHelper.getEntityManager(new Class[] {Post.class, PostComment.class});
        saveData(entityManager);

        entityManager.clear();

        entityManager.getTransaction().begin();

        Post post = entityManager.find(Post.class, 1L);
        post.getPostComments().size();

        entityManager.getTransaction().commit();
        entityManager.close();


        entityManager = PersistentHelper.getEntityManager(new Class[] {Post.class, PostComment.class});

        entityManager.getTransaction().begin();

        post.setTitle("Post 1 changed");
        post.getPostComments().get(0).setReview("Comment 1 changed");

        entityManager.merge(post);

        entityManager.getTransaction().commit();

    }

    public static void saveData(EntityManager entityManager) {
        entityManager.getTransaction().begin();

        Post post1 = new Post("Post 1");
        Post post2 = new Post("Post 2");

        entityManager.persist(post1);
        entityManager.persist(post2);

        PostComment postComment1 = new PostComment("Comment 1");
        PostComment postComment2 = new PostComment("Comment 2");
        PostComment postComment3 = new PostComment("Comment 3");
        PostComment postComment4 = new PostComment("Comment 4");

        postComment1.setPost(post1);
        postComment2.setPost(post1);
        postComment3.setPost(post2);
        postComment4.setPost(post2);

        entityManager.persist(postComment1);
        entityManager.persist(postComment2);
        entityManager.persist(postComment3);
        entityManager.persist(postComment4);

        entityManager.getTransaction().commit();
    }

    @Entity
    @Table(name = "post")
    public static class Post {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String title;

        @OneToMany(mappedBy = "post", cascade = CascadeType.MERGE)
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

        @Override
        public String toString() {
            return "Post{" +
                    "id=" + id +
                    ", title='" + title + '\'' +
                    '}';
        }
    }

    @Entity
    @Table(name = "post_comment")
    public static class PostComment {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String review;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "post_id")
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
            if (!(o instanceof PostComment)) return false;
            return id != null && id.equals(((PostComment) o).getId());
        }

        @Override
        public int hashCode() {
            return 31;
        }

        @Override
        public String toString() {
            return "PostComment{" +
                    "id=" + id +
                    ", review='" + review + '\'' +
                    ", post=" + post +
                    '}';
        }
    }
}
