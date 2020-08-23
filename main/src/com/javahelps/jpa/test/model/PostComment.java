package com.javahelps.jpa.test.model;

import javax.persistence.*;

@Entity(name = "PostComment")
@Table(name = "post_comment")
public class PostComment {
 
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
 
    private String review;
 
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
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

    @PrePersist
    private void prePersist() {
        System.out.println("PostComment pre persist");
    }

    @PreUpdate
    private void preUpdate() {
        System.out.println("PostComment pre update");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PostComment )) return false;
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