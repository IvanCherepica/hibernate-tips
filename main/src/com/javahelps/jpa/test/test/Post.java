package com.javahelps.jpa.test.test;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "Post")
@Table(name = "post")
class Post {

        @Id
        @GeneratedValue
        private Long id;

        private String title;

        public Post() {}

        public Post(String title) {
            this.title = title;
        }

        @ManyToMany
        private List<Tag> tags = new ArrayList<>();

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

        public List<Tag> getTags() {
            return tags;
        }

        public void setTags(List<Tag> tags) {
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
            if (!(o instanceof Post)) return false;
            return id != null && id.equals(((Post) o).getId());
        }

        @Override
        public int hashCode() {
            return 31;
        }
    }