package com.javahelps.jpa.test.result_set_mapping;

import com.javahelps.jpa.test.util.PersistentHelper;

import javax.persistence.*;
import java.util.List;

public class _1_Problem {
    public static void main(String[] args) {
        EntityManager entityManager = PersistentHelper.getEntityManager(new Class[] {Author.class});

        saveData(entityManager);
        entityManager.clear();

        {//используя * или явно указав в выборке все поля, имеюие отноение к сущноти мы можем заставить хибернейт
            // замапить сущнсоть целиком, будто бы это jpql запрос
            entityManager.getTransaction().begin();

            List<Author> authors = entityManager.createNativeQuery("SELECT * FROM author", Author.class).getResultList();

            authors.forEach(a -> System.out.println(a.getId()));

            entityManager.getTransaction().commit();
        }

        {//запрос выше записал в кэш первого уровня все выбранные сущности
            entityManager.getTransaction().begin();

            entityManager.find(Author.class, 1L);

            entityManager.getTransaction().commit();
        }

        entityManager.clear();

        {//проблема возникает, когда мы не можем использовать * или не можем обеспечить именное соответствие полей объекта
            // и столбцов в выборке
            entityManager.getTransaction().begin();

            List<Author> authors = entityManager.createNativeQuery("SELECT a.id as authorId, a.firstName, a.lastName, a.version FROM author a", Author.class).getResultList();

            authors.forEach(a -> System.out.println(a.getId()));

            entityManager.getTransaction().commit();
        }
    }

    private static void saveData(EntityManager entityManager) {
        entityManager.getTransaction().begin();

        Author author1 = new Author();
        Author author2 = new Author();

        entityManager.persist(author1);
        entityManager.persist(author2);

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
}
