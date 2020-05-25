package com.javahelps.jpa.test.test_context;


import com.javahelps.jpa.test.equals_and_hashcode.Solution;
import com.javahelps.jpa.test.util.PersistentHelper;

import javax.persistence.*;
import java.util.*;

public class Test2 {

    private static EntityManager entityManager;

    public static void main(String[] args) {
        entityManager = PersistentHelper.getEntityManager(new Class[] {Post.class, Tag.class});

        saveData();
    }

    private static void saveData() {

        entityManager.getTransaction().begin();

        Post post1 = new Post("post 1");
        Post post2 = new Post("post 2");

        entityManager.persist(post1);
        entityManager.persist(post2);

        Tag tag1 = new Tag("tag 1");
        Tag tag2 = new Tag("tag 2");

        entityManager.persist(tag1);
        entityManager.persist(tag2);

        post1.addTag(tag1);
        post1.addTag(tag2);
        post2.addTag(tag1);
        post2.addTag(tag2);

        entityManager.getTransaction().commit();
    }

    @Entity(name = "Post")
    @Table(name = "post")
    private static class Post {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String title;

        @ManyToMany
        private Set<Tag> tags = new HashSet<>();

        public Post() {}

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

        public Set<Tag> getTags() {
            return tags;
        }

        public void setTags(Set<Tag> tags) {
            this.tags = tags;
        }

        public void addTag(Tag tag) {
            tags.add(tag);
            tag.getPosts().add(this);
        }

        public void removeTag(Tag tag) {
            tags.remove(tag);
            tag.getPosts().remove(this);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Post post = (Post) o;
            return Objects.equals(id, post.id) &&
                    Objects.equals(title, post.title) &&
                    Objects.equals(tags, post.tags);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id, title, tags);
        }
    }

    @Entity(name = "Tag")
    @Table(name = "tag")
    private static class Tag {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String name;

        @ManyToMany
        private Set<Post> posts = new HashSet<>();

        public Tag() {}

        public Tag(String name) {
            this.name = name;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public Set<Post> getPosts() {
            return posts;
        }

        public void setPosts(Set<Post> posts) {
            this.posts = posts;
        }

//        public void addPost(Post post) {
//            posts.add(post);
//            tag.getPosts().add(this);
//        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Tag tag = (Tag) o;
            return Objects.equals(id, tag.id) &&
                    Objects.equals(name, tag.name) &&
                    Objects.equals(posts, tag.posts);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id, name, posts);
        }
    }
}
