package com.javahelps.jpa.test.element_list;

import com.javahelps.jpa.test.remove.ByJPQLSolution2;
import com.javahelps.jpa.test.util.PersistentHelper;
import com.vladmihalcea.hibernate.type.array.ListArrayType;
import org.hibernate.Query;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.transform.ResultTransformer;

import javax.persistence.*;
import java.util.*;

public class PostgresArrayType {

    private static EntityManager entityManager = PersistentHelper.getEntityManager(new Class[] {Morpheme.class});

    public static void main(String[] args) {
        persistData();

        entityManager.clear();
        System.out.println();
        System.out.println("===============================================================");
        System.out.println();

        selectData();

        entityManager.clear();
        System.out.println();
        System.out.println("===============================================================");
        System.out.println();

        updateData();

        entityManager.clear();
        System.out.println();
        System.out.println("===============================================================");
        System.out.println();

        selectHql();

        entityManager.clear();
        System.out.println();
        System.out.println("===============================================================");
        System.out.println();

        selectMorphemeList();

        entityManager.clear();
        System.out.println();
        System.out.println("===============================================================");
        System.out.println();

        selectConstructorExpression();

        entityManager.clear();
        System.out.println();
        System.out.println("===============================================================");
        System.out.println();

        selectResultTransformerHql();
    }

    private static void persistData() {
        entityManager.getTransaction().begin();

        Morpheme morpheme = new Morpheme();
        morpheme.setName("приставки");
        morpheme.setMorphemeList(Arrays.asList("пре", "при"));

        entityManager.persist(morpheme);
        //получаем два запроса: певый на сохранение сущности, второй на update колонки с массивом
        entityManager.getTransaction().commit();
    }

    private static void selectData() {
        entityManager.getTransaction().begin();

        Morpheme morpheme = entityManager.find(Morpheme.class, 1L);
        System.out.println(morpheme.getMorphemeList());
        //Один запрос, всё ок. Но есть один нюанс: возможность ленивой загрузки коллекции у нас отсутствует
        entityManager.getTransaction().commit();
    }

    private static void updateData() {
        entityManager.getTransaction().begin();

        Morpheme morpheme = entityManager.find(Morpheme.class, 1L);
        morpheme.getMorphemeList().add("за");
        //Проблема в том, что происходит update сразу всего массива. Нативные инструменты postgres дря работы с массивами игнорируются
        entityManager.getTransaction().commit();
    }

    private static void selectHql() {
        entityManager.getTransaction().begin();

        Morpheme morpheme = entityManager.createQuery(
                "SELECT m FROM " + Morpheme.class.getName() + " m WHERE m.id = 1",
                Morpheme.class
        )
                .getSingleResult();
        System.out.println(morpheme.getMorphemeList());

        entityManager.getTransaction().commit();
    }

    private static void selectMorphemeList() {
        entityManager.getTransaction().begin();

        List<String> morphemeList = (List<String>) entityManager.createQuery(
                "SELECT m.morphemeList FROM " + Morpheme.class.getName() + " m WHERE m.id = 1"
        )
                .getSingleResult();
        System.out.println(morphemeList);

        entityManager.getTransaction().commit();
    }

    private static void selectConstructorExpression() {
        entityManager.getTransaction().begin();

        MorphemeDto morphemeDto = entityManager.createQuery(
                "SELECT new com.javahelps.jpa.test.element_list.MorphemeDto(m.id, m.morphemeList) FROM " + Morpheme.class.getName() + " m WHERE m.id = 1",
                MorphemeDto.class
        )
                .getSingleResult();
        System.out.println(morphemeDto.getMorphemes());
        //можем использовать constructor expression, если в качестве параметра принимаетм Object
        entityManager.getTransaction().commit();
    }

    private static void selectResultTransformerHql() {
        entityManager.getTransaction().begin();

        MorphemeDto morphemeDto = (MorphemeDto) entityManager.createQuery(
                "SELECT m.id, m.morphemeList FROM " + Morpheme.class.getName() + " m WHERE m.id = 1"
        )
                .unwrap(Query.class)
                .setResultTransformer(new ResultTransformer() {


                    @Override
                    public MorphemeDto transformTuple(Object[] tuple, String[] aliases) {
                        MorphemeDto dto = new MorphemeDto();
                        dto.setId((Long) tuple[0]);
                        dto.setMorphemes((List<String>) tuple[1]);

                        return dto;
                    }

                    @Override
                    public List<MorphemeDto> transformList(List collection) {
                        return collection;
                    }
                })
                .getSingleResult();

        System.out.println(morphemeDto.getMorphemes());

        entityManager.getTransaction().commit();
    }


    @Entity
    @Table(name = "morpheme")
    @TypeDef(
            name = "list-array",
            typeClass = ListArrayType.class
    )
    private static class Morpheme {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String name;

        @Type(type = "list-array")
        @Column(
                name = "morpheme_array",
                columnDefinition = "text[]"
        )
        private List<String> morphemeList;

        public Morpheme() {
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

        public List<String> getMorphemeList() {
            return morphemeList;
        }

        public void setMorphemeList(List<String> morphemeList) {
            this.morphemeList = morphemeList;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Morpheme morpheme = (Morpheme) o;
            return Objects.equals(id, morpheme.id) &&
                    Objects.equals(name, morpheme.name) &&
                    Objects.equals(morphemeList, morpheme.morphemeList);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id, name, morphemeList);
        }
    }


}
