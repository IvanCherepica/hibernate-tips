package com.javahelps.jpa.test.cascade.listeners.model;

import com.javahelps.jpa.test.cascade.listeners.PostListener;

import javax.persistence.*;

@Entity
    @Table(name = "post")
@EntityListeners(PostListener.class)
    public class Post {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String title;

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

        @Override
        public String toString() {
            return "Post{" +
                    "id=" + id +
                    ", title='" + title + '\'' +
                    '}';
        }
    }