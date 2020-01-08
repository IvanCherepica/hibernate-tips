package com.javahelps.jpa.test.dto_mapping.tuple_example;

import com.javahelps.jpa.test.dto_mapping.Util;
import com.javahelps.jpa.test.dto_mapping.dto.AnswerDto;
import com.javahelps.jpa.test.dto_mapping.dto.TaskDto;
import com.javahelps.jpa.test.dto_mapping.model.Answer;
import com.javahelps.jpa.test.dto_mapping.model.Task;
import com.javahelps.jpa.test.util.PersistentHelper;
import org.hibernate.Query;
import org.hibernate.transform.ResultTransformer;

import javax.persistence.EntityManager;
import javax.persistence.Tuple;
import java.math.BigInteger;
import java.util.List;

public class SimpleTuple {
    public static void main(String[] args) {
        EntityManager entityManager = PersistentHelper.getEntityManager(new Class[]{Answer.class, Task.class});

        Util.saveData(entityManager);
        entityManager.clear();

        entityManager.clear();

        {//довольно удобная альтернатива, если нам не нужна отдельная дто и нас не беспокоит безопасность типов
            entityManager.getTransaction().begin();

            List<Tuple> answerTuple = entityManager.createQuery(
                    "SELECT a.id AS id, a.answer AS answer FROM Answer a",
                    Tuple.class
            )
                    .getResultList();

            answerTuple.forEach(t -> System.out.println(t.get("id") + " " + t.get("answer")));

            //минусы данного подхода проявляются, когда нам нужно заниматься приведением типов из резалтеста
            //нужно явно приводить каждый тип
            for (Tuple tuple : answerTuple) {
                long id = (Long) tuple.get("id");
                String answer = (String) tuple.get("answer");

                System.out.println(id +" "+ answer);
            }

            entityManager.getTransaction().commit();
        }

        entityManager.clear();

        {
            entityManager.getTransaction().begin();

            List<Tuple> answerTuple = entityManager.createNativeQuery(
                    "SELECT a.id AS id, a.answer AS answer FROM answer a",
                    Tuple.class
            )
                    .getResultList();

            //однако, если мы используем нативные запросы, то здесь нам ещё будет нужно знать, в каких типах
            //jdbc возвращает тот или иной тип данных. в данном случае, тип long будет возвращаться как Bignteger
            for (Tuple tuple : answerTuple) {
                long id = ((BigInteger) tuple.get("id")).longValue();
                String answer = (String) tuple.get("answer");

                System.out.println(id +" "+ answer);
            }

            entityManager.getTransaction().commit();
        }

    }
}
