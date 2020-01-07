package com.javahelps.jpa.test.dto_mapping.jpa_constructor_expression;

import com.javahelps.jpa.test.dto_mapping.Util;
import com.javahelps.jpa.test.dto_mapping.dto.AnswerDto;
import com.javahelps.jpa.test.dto_mapping.dto.AnswerDtoWithTask;
import com.javahelps.jpa.test.dto_mapping.dto.TaskDto;
import com.javahelps.jpa.test.dto_mapping.dto.TaskDtoWithAnswer;
import com.javahelps.jpa.test.dto_mapping.model.Answer;
import com.javahelps.jpa.test.dto_mapping.model.Task;
import com.javahelps.jpa.test.util.PersistentHelper;

import javax.persistence.EntityManager;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ComplexMappingProblem {
    public static void main(String[] args) {
        EntityManager entityManager = PersistentHelper.getEntityManager(new Class[]{Answer.class, Task.class});

        Util.saveData(entityManager);
        entityManager.clear();

        {//при выборке дто, которая содержит объект сущности мы имеем сходу n+1 проблему:
            //Дополнительный запрос, выполняемый хибернейтом на присоединение сущности к дто
            //Проблема не решается использованием INNER JOIN, т.к. нет корневой сущности. Вместо неё dto
            entityManager.getTransaction().begin();

            List<AnswerDtoWithTask> answerDtoWithTasks = entityManager.createQuery(
                    "SELECT new com.javahelps.jpa.test.dto_mapping.dto.AnswerDtoWithTask(" +
                            "a.id, " +
                            "a.answer, " +
                            "a.task" +
                            ") FROM "+Answer.class.getName()+" a",
                    AnswerDtoWithTask.class
            )
                    .getResultList();

            for (AnswerDtoWithTask answerDtoWithTask : answerDtoWithTasks) {
                System.out.println(answerDtoWithTask);
                System.out.println(answerDtoWithTask.getTask());
            }

            entityManager.getTransaction().commit();
        }

        entityManager.clear();

        {//jpa не позволяет использовать в качестве аргумента конструктора коллекцию,
            //https://stackoverflow.com/questions/2678634/is-this-possible-jpa-hibernate-query-with-list-property-in-result/2678973#2678973
            entityManager.getTransaction().begin();

            //подобным образом мы не можем составить запрос. хибернейт не понимает, что такое t.answers
//            List<TaskDtoWithAnswer> taskDtoWithAnswers = entityManager.createQuery(
//                    "SELECT new com.javahelps.jpa.test.dto_mapping.dto.TaskDtoWithAnswer(" +
//                            "t.id, " +
//                            "t.title, " +
//                            "t.answers " +
//                            ") FROM "+Task.class.getName()+" t",
//                    TaskDtoWithAnswer.class
//            )
//                    .getResultList();

            //пробуем хакнуть это ограничение, создав конструктор, который принимает Object вместо коллекции
            //однако, вместо коллекции мы получим отдельный объект ответа, который будет присоединен к дтохе
            //количество дтох вырстет и станет равным количеству дто * на количество присоединяемых к каждой дто сузностей
//            List<TaskDtoWithAnswer> taskDtoWithAnswers = entityManager.createQuery(
//                    "SELECT new com.javahelps.jpa.test.dto_mapping.dto.TaskDtoWithAnswer(" +
//                            "t.id, " +
//                            "t.title, " +
//                            "a " +
//                            ") FROM "+Task.class.getName()+" t JOIN t.answers a",
//                    TaskDtoWithAnswer.class
//            )
//                    .getResultList();

            //при подобном подходе мы так же сталкиваемся с n+1 проблемой из-за необходимости подгузки связанной сущности
            //для маппинга. + так же приобретаем один дополнительный запрос к базе
            //код для довольно простой операции занимает слишком много места
            List<TaskDtoWithAnswer> taskDtoWithAnswers = entityManager.createQuery(
                    "SELECT new com.javahelps.jpa.test.dto_mapping.dto.TaskDtoWithAnswer(" +
                            "t.id, " +
                            "t.title " +
                            ") FROM "+Task.class.getName()+" t",
                    TaskDtoWithAnswer.class
            )
                    .getResultList();

            List<Long> ids = taskDtoWithAnswers.stream().mapToLong(TaskDtoWithAnswer::getId).boxed().collect(Collectors.toList());

            List<Answer> answers = entityManager.createQuery(
                    "FROM Answer a WHERE a.task.id IN (:ids)",
                    Answer.class
            )
                    .setParameter("ids", ids)
                    .getResultList();

            taskDtoWithAnswers.forEach(tdwa -> tdwa.setAnswers(
                    answers.stream().filter(a -> a.getTask().getId() == tdwa.getId()).collect(Collectors.toList())
            ));

            for (TaskDtoWithAnswer taskDtoWithAnswer : taskDtoWithAnswers) {
                System.out.println(taskDtoWithAnswer);

                for (Answer answer : taskDtoWithAnswer.getAnswers()) {
                    System.out.println(answer);
                }
            }

            entityManager.getTransaction().commit();
        }

    }
}
