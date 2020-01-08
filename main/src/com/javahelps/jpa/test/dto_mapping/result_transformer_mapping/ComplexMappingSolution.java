package com.javahelps.jpa.test.dto_mapping.result_transformer_mapping;

import com.javahelps.jpa.test.dto_mapping.Util;
import com.javahelps.jpa.test.dto_mapping.dto.AnswerDto;
import com.javahelps.jpa.test.dto_mapping.dto.AnswerDtoWithTask;
import com.javahelps.jpa.test.dto_mapping.dto.TaskDtoWithAnswer;
import com.javahelps.jpa.test.dto_mapping.dto.TaskDtoWithAnswersDto;
import com.javahelps.jpa.test.dto_mapping.model.Answer;
import com.javahelps.jpa.test.dto_mapping.model.Task;
import com.javahelps.jpa.test.util.PersistentHelper;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.transform.ResultTransformer;
import org.hibernate.type.StandardBasicTypes;

import javax.persistence.EntityManager;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ComplexMappingSolution {
    public static void main(String[] args) {
        EntityManager entityManager = PersistentHelper.getEntityManager(new Class[]{Answer.class, Task.class});

        Util.saveData(entityManager);
        entityManager.clear();

        {//используя нативный запрос - убираем n+1 проблему
            entityManager.getTransaction().begin();


            List<TaskDtoWithAnswer> taskDtoWithAnswers = entityManager.createNativeQuery(
                    "SELECT t.id AS taskId, t.title, a.id AS answerId, a.answer " +
                            "FROM task t JOIN answer a on t.id = a.task_id"
            )
                    .unwrap(SQLQuery.class)
                    .setResultTransformer(new TaskDtoWithAnswerTransformerForNativeQuery())
                    .list();


            for (TaskDtoWithAnswer taskDtoWithAnswer : taskDtoWithAnswers) {
                System.out.println(taskDtoWithAnswer);

                for (Answer answer : taskDtoWithAnswer.getAnswers()) {
                    System.out.println(answer);
                }
            }

            entityManager.getTransaction().commit();
        }

        entityManager.clear();

        {//из этого примера тоже следует исключить работу с сущностью. не смотря на то
            //что в примере выше сущность создается руками - она не перестает быть сущностью, даже в переходном стостоянии
            //мы проделывать операции с ней, которые будет оптравлять запросы в бл, что противоречит ридонли логике дто
            entityManager.getTransaction().begin();


            List<TaskDtoWithAnswersDto> taskDtoWithAnswersDtos = entityManager.createNativeQuery(
                    "SELECT t.id AS taskId, t.title, a.id AS answerId, a.answer " +
                            "FROM task t JOIN answer a on t.id = a.task_id"
            )
                    .unwrap(SQLQuery.class)
                    .setResultTransformer(new TaskDtoWithAnswersDtoTransformerForNativeQuery())
                    .list();


            for (TaskDtoWithAnswersDto taskDtoWithAnswersDto : taskDtoWithAnswersDtos) {
                System.out.println(taskDtoWithAnswersDto);

                for (AnswerDto answerDto : taskDtoWithAnswersDto.getAnswerDtos()) {
                    System.out.println(answerDto);
                }
            }

            entityManager.getTransaction().commit();
        }

        {//из этого примера тоже следует исключить работу с сущностью. не смотря на то
            //что в примере выше сущность создается руками - она не перестает быть сущностью, даже в переходном стостоянии
            //мы проделывать операции с ней, которые будет оптравлять запросы в бл, что противоречит ридонли логике дто
            entityManager.getTransaction().begin();


            List<TaskDtoWithAnswersDto> taskDtoWithAnswersDtos = entityManager.createQuery(
                    "SELECT t.id AS taskId, t.title, a.id AS answerId, a.answer " +
                            "FROM Task t JOIN Answer a on t.id = a.task.id"
            )
                    .unwrap(Query.class)
                    .setResultTransformer(new TaskDtoWithAnswerDtosTransformerForJPQLQuery())
                    .list();


            for (TaskDtoWithAnswersDto taskDtoWithAnswersDto : taskDtoWithAnswersDtos) {
                System.out.println(taskDtoWithAnswersDto);

                for (AnswerDto answerDto : taskDtoWithAnswersDto.getAnswerDtos()) {
                    System.out.println(answerDto);
                }
            }

            entityManager.getTransaction().commit();
        }
    }

    private static class TaskDtoWithAnswerTransformerForNativeQuery implements ResultTransformer {

        private Map<Long, List<Answer>> answerMap = new HashMap<>();

        private List<TaskDtoWithAnswer> roots = new ArrayList<>();

        @Override
        public Object transformTuple(Object[] tuple, String[] aliaces) {
            long taskId = ((BigInteger) tuple[0]).longValue();
            String title =  (String) tuple[1];

            long answerId = ((BigInteger) tuple[2]).longValue();
            String answer = (String) tuple[3];

            Answer answer1 = new Answer(answer);
            answer1.setId(answerId);

            TaskDtoWithAnswer taskDtoWithAnswer = new TaskDtoWithAnswer(taskId, title);

            if (!answerMap.containsKey(taskId)) {
                roots.add(taskDtoWithAnswer);
                answerMap.put(taskId, new ArrayList<>());
            }

            answerMap.get(taskId).add(answer1);
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

    private static class TaskDtoWithAnswersDtoTransformerForNativeQuery implements ResultTransformer {

        private Map<Long, List<AnswerDto>> answerMap = new HashMap<>();

        private List<TaskDtoWithAnswersDto> roots = new ArrayList<>();

        @Override
        public Object transformTuple(Object[] tuple, String[] aliaces) {
            long taskId = ((BigInteger) tuple[0]).longValue();
            String title =  (String) tuple[1];

            long answerId = ((BigInteger) tuple[2]).longValue();
            String answer = (String) tuple[3];

            TaskDtoWithAnswersDto taskDtoWithAnswer = new TaskDtoWithAnswersDto(taskId, title);

            if (!answerMap.containsKey(taskId)) {
                roots.add(taskDtoWithAnswer);
                answerMap.put(taskId, new ArrayList<>());
            }

            answerMap.get(taskId).add(new AnswerDto(answerId, answer));
            return taskDtoWithAnswer;
        }

        @Override
        public List transformList(List list) {
            for (TaskDtoWithAnswersDto taskDtoWithAnswersDto : roots) {
                taskDtoWithAnswersDto.setAnswerDtos(answerMap.get(taskDtoWithAnswersDto.getId()));
            }

            return roots;
        }
    }

    private static class TaskDtoWithAnswerDtosTransformerForJPQLQuery implements ResultTransformer {

        private Map<Long, List<AnswerDto>> answerMap = new HashMap<>();

        private List<TaskDtoWithAnswersDto> roots = new ArrayList<>();

        @Override
        public Object transformTuple(Object[] tuple, String[] aliaces) {
            long taskId = (long) tuple[0];
            String title =  (String) tuple[1];

            long answerId = (long) tuple[2];
            String answer = (String) tuple[3];

            TaskDtoWithAnswersDto taskDtoWithAnswer = new TaskDtoWithAnswersDto(taskId, title);

            if (!answerMap.containsKey(taskId)) {
                roots.add(taskDtoWithAnswer);
                answerMap.put(taskId, new ArrayList<>());
            }

            answerMap.get(taskId).add(new AnswerDto(answerId, answer));
            return taskDtoWithAnswer;
        }

        @Override
        public List transformList(List list) {
            for (TaskDtoWithAnswersDto taskDtoWithAnswersDto : roots) {
                taskDtoWithAnswersDto.setAnswerDtos(answerMap.get(taskDtoWithAnswersDto.getId()));
            }

            return roots;
        }
    }
}
