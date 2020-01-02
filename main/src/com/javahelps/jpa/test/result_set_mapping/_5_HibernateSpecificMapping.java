package com.javahelps.jpa.test.result_set_mapping;

import com.javahelps.jpa.test.util.PersistentHelper;
import org.hibernate.Session;
import org.hibernate.type.StandardBasicTypes;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

public class _5_HibernateSpecificMapping {
    public static void main(String[] args) {
        EntityManager entityManager = PersistentHelper.getEntityManager(new Class[] {Author.class, Book.class});

        saveData(entityManager);
        entityManager.clear();

        {//предположим, мы хотим избежать n+1 проблемы, но так же мы хотим оставить все возможности нативного запроса
            //в таком случае, нам, как и при использовании hql нужно будет сдеать join на конкрентную связанную сущснотсь
            entityManager.getTransaction().begin();

            System.out.println();
            System.out.println("Before joined query");
            System.out.println();



            List<Object[]> results = ((Session)entityManager.getDelegate())
                    .createSQLQuery("SELECT {b.*}, {a.*} FROM book b JOIN author a ON b.author_id = a.id")
                    .addEntity("b", Book.class)
                    .addEntity("a", Author.class)
                    .list();

            List<Book> books = new ArrayList<>(results.size());

            results.forEach((record) -> {
                Book book = (Book) record[0];
                Author author = (Author) record[1];

                books.add(book);
            });

            System.out.println();
            System.out.println("After joined query");
            System.out.println();

            //никакого n+1
            books.forEach(b -> System.out.println(b.getAuthor().getId()));

            entityManager.getTransaction().commit();
        }

        {//используя sqlresultmapping, даже при получении листа bjects мы можем иметь к загруженным данным доступ
            //из кэша первого уровня
            entityManager.getTransaction().begin();

            System.out.println();
            System.out.println("Before check first level cash after 1 query");
            System.out.println();

            entityManager.find(Author.class, 1L);
            entityManager.find(Book.class, 1L);

            System.out.println();
            System.out.println("After check first level cash after 1 query");
            System.out.println();

            entityManager.getTransaction().commit();
        }

        entityManager.clear();

        {
            entityManager.getTransaction().begin();

            System.out.println();
            System.out.println("Before joined query");
            System.out.println();

            List<Object[]> results = ((Session)entityManager.getDelegate())
                    .createSQLQuery("SELECT {a.*}, count(b.id) as bookCount FROM book b JOIN author a ON b.author_id = a.id GROUP BY a.id, a.firstName, a.lastName, a.version")
                    .addEntity("a", Author.class)
                    .addScalar("bookCount", StandardBasicTypes.LONG)
                    .list();
            results.forEach((record) -> {
                Author author = (Author) record[0];
                Long bookCount = (Long) record[1];
                System.out.println("Author: ID [" + author.getId() + "] firstName [" + author.getFirstName() + "] lastName [" + author.getLastName() + "] number of books [" + bookCount + "]");
            });

            System.out.println();
            System.out.println("After joined query");
            System.out.println();

            entityManager.getTransaction().commit();
        }
    }

    private static void saveData(EntityManager entityManager) {
        entityManager.getTransaction().begin();

        Author author1 = new Author();
        Author author2 = new Author();

        Book book1 = new Book();
        Book book2 = new Book();
        Book book3 = new Book();
        Book book4 = new Book();

        entityManager.persist(book1);
        entityManager.persist(book2);
        entityManager.persist(book3);
        entityManager.persist(book4);

        entityManager.persist(author1);
        entityManager.persist(author2);

        book1.setAuthor(author1);
        book2.setAuthor(author1);
        book3.setAuthor(author2);
        book4.setAuthor(author2);

        entityManager.getTransaction().commit();
    }

    @Entity
    @Table(name = "author")
    private static class Author {

        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        @Column(name = "id", updatable = false, nullable = false)
        private Long id;
        @Version
        @Column(name = "version")
        private int version;

        @Column
        private String firstName;

        @Column
        private String lastName;

        public Long getId() {
            return this.id;
        }

        public void setId(final Long id) {
            this.id = id;
        }

        public int getVersion() {
            return this.version;
        }

        public void setVersion(final int version) {
            this.version = version;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof Author)) {
                return false;
            }
            Author other = (Author) obj;
            if (id != null) {
                if (!id.equals(other.id)) {
                    return false;
                }
            }
            return true;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((id == null) ? 0 : id.hashCode());
            return result;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        @Override
        public String toString() {
            String result = getClass().getSimpleName() + " ";
            if (firstName != null && !firstName.trim().isEmpty()) {
                result += "firstName: " + firstName;
            }
            if (lastName != null && !lastName.trim().isEmpty()) {
                result += ", lastName: " + lastName;
            }
            return result;
        }
    }

    @Entity
    @Table(name = "book")
    private static class Book {

        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        @Column(name = "id", updatable = false, nullable = false)
        private Long id;
        @Version
        @Column(name = "version")
        private int version;

        @Column
        private String title;

        @ManyToOne(fetch = FetchType.LAZY)
        private Author author;

        public Long getId() {
            return this.id;
        }

        public void setId(final Long id) {
            this.id = id;
        }

        public int getVersion() {
            return this.version;
        }

        public void setVersion(final int version) {
            this.version = version;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public Author getAuthor() {
            return this.author;
        }

        public void setAuthor(final Author author) {
            this.author = author;
        }

        @Override
        public String toString() {
            String result = getClass().getSimpleName() + " ";
            if (title != null && !title.trim().isEmpty()) {
                result += "title: " + title;
            }
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof Book)) {
                return false;
            }
            Book other = (Book) obj;
            if (id != null) {
                if (!id.equals(other.id)) {
                    return false;
                }
            }
            return true;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((id == null) ? 0 : id.hashCode());
            return result;
        }

    }
}
