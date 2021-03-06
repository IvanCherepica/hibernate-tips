package com.javahelps.jpa.test.many_to_many;

import com.javahelps.jpa.test.util.PersistentHelper;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class _8_Problem {
    public static void main(String[] args) {
        EntityManager entityManager = PersistentHelper.getEntityManager(new Class[] {Post.class, Tag.class});

        saveObjects(entityManager);
        entityManager.clear();

        entityManager.getTransaction().begin();
        System.out.println();

        //достаем Post, в котором только два Tag id=1 и id=2
        Post post = entityManager.find(Post.class, 2L);
        Tag tag = entityManager.find(Tag.class, 3L);

        //добавляем tag id=1
        post.addTag(tag);

        //Hibernate сначала удаляет все из смежной таблицы, где posts_id=2
        //А заме отправляет 3 insert запоса
        entityManager.getTransaction().commit();
    }

    private static void saveObjects(EntityManager entityManager) {
        entityManager.getTransaction().begin();

        Post post1 = new Post("Post 1");
        Post post2 = new Post("Post 2");
        Post post3 = new Post("Post 3");

        Tag tag1 = new Tag("Tag 1");
        Tag tag2 = new Tag("Tag 2");
        Tag tag3 = new Tag("Tag 3");

        //ушел от создания листов. что бы использовать утилитный метод add
        post1.addTag(tag1);
        post1.addTag(tag2);
        post1.addTag(tag3);

        post2.addTag(tag2);
        post2.addTag(tag3);

        post3.addTag(tag3);

        entityManager.persist(post1);
        entityManager.persist(post2);
        entityManager.persist(post3);

        //исключений не возникает - теперь всё сохраняется в базу
        entityManager.getTransaction().commit();
    }

    @Entity(name = "Post")
    @Table(name = "post")
    private static class Post {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String title;

        public Post() {}

        public Post(String title) {
            this.title = title;
        }

        @ManyToMany(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
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

        public void remove() {
            for(Tag tag : new ArrayList<>(tags)) {
                removeTag(tag);
            }
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

    @Entity(name = "Tag")
    @Table(name = "tag")
    private static class Tag {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String name;

        @ManyToMany(mappedBy = "tags", cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
        private List<Post> posts = new ArrayList<>();

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

        public List<Post> getPosts() {
            return posts;
        }

        public void setPosts(List<Post> posts) {
            this.posts = posts;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Tag tag = (Tag) o;
            return Objects.equals(name, tag.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name);
        }
    }
}
