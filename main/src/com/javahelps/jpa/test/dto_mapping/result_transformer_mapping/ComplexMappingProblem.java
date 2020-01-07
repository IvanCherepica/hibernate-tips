package com.javahelps.jpa.test.dto_mapping.result_transformer_mapping;

import com.javahelps.jpa.test.dto_mapping.Util;
import com.javahelps.jpa.test.dto_mapping.dto.AnswerDtoWithTask;
import com.javahelps.jpa.test.dto_mapping.dto.TaskDto;
import com.javahelps.jpa.test.dto_mapping.dto.TaskDtoWithAnswer;
import com.javahelps.jpa.test.dto_mapping.model.Answer;
import com.javahelps.jpa.test.dto_mapping.model.Task;
import com.javahelps.jpa.test.util.PersistentHelper;
import org.hibernate.Query;
import org.hibernate.jpa.QueryHints;
import org.hibernate.transform.ResultTransformer;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ComplexMappingProblem {
    public static void main(String[] args) {
        EntityManager entityManager = PersistentHelper.getEntityManager(new Class[]{Answer.class, Task.class});

        Util.saveData(entityManager);
        entityManager.clear();

        {//большущий динозавр по сравнению с contructor expression из jpa, однако, у данного подхода есть очень важное
            //преимущество - мы не получаем n+1 дополнительных запросов
            entityManager.getTransaction().begin();

            List<AnswerDtoWithTask> answerDtoWithTasks = entityManager.createQuery(
                    "SELECT a.id, a.answer, a.task FROM Answer a"
            )
                    .unwrap(Query.class)
                    .setResultTransformer(
                            new ResultTransformer() {
                                @Override
                                public Object transformTuple(
                                        Object[] tuple,
                                        String[] aliases) {
                                    return new AnswerDtoWithTask(
                                            (Long) tuple[0],
                                            (String) tuple[1],
                                            (Task) tuple[2]
                                    );
                                }

                                @Override
                                public List transformList(List collection) {
                                    return collection;
                                }
                            }
                    )
                    .list();

            for (AnswerDtoWithTask answerDtoWithTask : answerDtoWithTasks) {
                System.out.println(answerDtoWithTask);
                System.out.println(answerDtoWithTask.getTask());
            }

            entityManager.getTransaction().commit();
        }

        entityManager.clear();

        {//в подобном случае, однако, как бы мы ни хотели - получается n+1, т.к. хибернейту не к чему выполнить
            //join fetch. Граф мы тоже использовать не можем, т.к. корневой класс не является сущностью
            entityManager.getTransaction().begin();


            List<TaskDtoWithAnswer> taskDtoWithAnswers = entityManager.createQuery(
                    "SELECT t.id, t.title, a FROM Task t JOIN t.answers a"
            )
                    .unwrap(Query.class)
                    .setResultTransformer(new TestTransformer())
                    .list();


            for (TaskDtoWithAnswer taskDtoWithAnswer : taskDtoWithAnswers) {
                System.out.println(taskDtoWithAnswer);

                for (Answer answer : taskDtoWithAnswer.getAnswers()) {
                    System.out.println(answer);
                }
            }

            entityManager.getTransaction().commit();
        }
    }

    private static class TestTransformer implements ResultTransformer {

        private Map<Long, List<Answer>> answerMap = new HashMap<>();

        private List<TaskDtoWithAnswer> roots = new ArrayList<>();

        @Override
        public Object transformTuple(Object[] tuple, String[] aliaces) {
            Long id = (Long) tuple[0];
            String title =  (String) tuple[1];
            Answer answer = (Answer) tuple[2];

            TaskDtoWithAnswer taskDtoWithAnswer = new TaskDtoWithAnswer(id, title);

            if (!answerMap.containsKey(id)) {
                roots.add(taskDtoWithAnswer);
                answerMap.put(id, new ArrayList<>());
            }

            answerMap.get(id).add(answer);
            return taskDtoWithAnswer;
        }

        @Override
        public List transformList(List list) {
            for (TaskDtoWithAnswer taskDtoWithAnswer : roots) {
                taskDtoWithAnswer.setAnswers(answerMap.get(taskDtoWithAnswer.getId()));
            }

            return roots;
        }
    }
}
