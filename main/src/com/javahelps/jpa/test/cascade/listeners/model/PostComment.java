package com.javahelps.jpa.test.cascade.listeners.model;

import com.javahelps.jpa.test.cascade.listeners.PostListener;

import javax.persistence.*;

@Entity
    @Table(name = "post_comment")
    public class PostComment {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String review;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "post_id")
        private Post post;

        @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
        @JoinColumn(name = "related_theme_id")
        private RelatedTheme relatedTheme;

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

        public RelatedTheme getRelatedTheme() {
            return relatedTheme;
        }

        public void setRelatedTheme(RelatedTheme relatedTheme) {
            this.relatedTheme = relatedTheme;
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