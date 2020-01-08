package com.javahelps.jpa.test.dto_mapping.tuple_example;

import com.javahelps.jpa.test.dto_mapping.Util;
import com.javahelps.jpa.test.dto_mapping.model.Answer;
import com.javahelps.jpa.test.dto_mapping.model.Task;
import com.javahelps.jpa.test.util.PersistentHelper;

import javax.persistence.EntityManager;
import javax.persistence.Tuple;
import java.math.BigInteger;
import java.util.List;

public class ComplexTuple {
    public static void main(String[] args) {
        EntityManager entityManager = PersistentHelper.getEntityManager(new Class[]{Answer.class, Task.class});

        Util.saveData(entityManager);
        entityManager.clear();

        entityManager.clear();

        {//довольно удобная альтернатива, если нам не нужна отдельная дто и нас не беспокоит безопасность типов
            entityManager.getTransaction().begin();

            List<Tuple> answerTuple = entityManager.createQuery(
                    "SELECT a.id AS answerId, a.answer AS answer, a.task.id AS taskId, a.task.title AS title FROM Answer a",
                    Tuple.class
            )
                    .getResultList();

            answerTuple.forEach(t ->
                    System.out.println("answer: {"+t.get("answerId") + " " + t.get("answer") +
                    "}; task: {"+t.get("taskId")+" "+t.get("title") + "}"));

            entityManager.getTransaction().commit();
        }

        entityManager.clear();

        {//та же история с нативным запросом. нужно кастить каждый алиас
            entityManager.getTransaction().begin();

            List<Tuple> answerTuple = entityManager.createNativeQuery(
                    "SELECT a.id AS answerId, a.answer AS answer, t.id AS taskId, t.title AS title " +
                            "FROM answer a LEFT JOIN task t on a.task_id = t.id",
                    Tuple.class
            )
                    .getResultList();

            for (Tuple tuple : answerTuple) {
                long answerId = ((BigInteger) tuple.get("answerId")).longValue();
                String answer = (String) tuple.get("answer");
                long taskId = ((BigInteger) tuple.get("taskId")).longValue();
                String title = (String) tuple.get("title");

                System.out.println("answer: {"+answerId + " " + answer +
                        "}; task: {"+taskId+" "+title + "}");
            }

            entityManager.getTransaction().commit();
        }

        entityManager.clear();

        {//использовать тплю как дто с коллекцией не выйдет - тупля является отображением одного кортежа, и если нужен более
            //сложный маппинг, то лучше присмотреться к трансфлрмеру
            entityManager.getTransaction().begin();

            List<Tuple> answerTuple = entityManager.createNativeQuery(
                    "SELECT t.id AS taskId, t.title AS title, a.id AS answerId, a.answer AS answer " +
                            "FROM task t LEFT JOIN answer a on t.id = a.task_id",
                    Tuple.class
            )
                    .getResultList();

            for (Tuple tuple : answerTuple) {
                long answerId = ((BigInteger) tuple.get("answerId")).longValue();
                String answer = (String) tuple.get("answer");
                long taskId = ((BigInteger) tuple.get("taskId")).longValue();
                String title = (String) tuple.get("title");

                System.out.println("answer: {"+answerId + " " + answer +
                        "}; task: {"+taskId+" "+title + "}");
            }

            entityManager.getTransaction().commit();
        }
    }
}
