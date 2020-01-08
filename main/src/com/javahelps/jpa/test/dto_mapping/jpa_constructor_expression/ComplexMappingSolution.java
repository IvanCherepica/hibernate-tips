package com.javahelps.jpa.test.dto_mapping.jpa_constructor_expression;

import com.javahelps.jpa.test.dto_mapping.Util;
import com.javahelps.jpa.test.dto_mapping.dto.AnswerDto;
import com.javahelps.jpa.test.dto_mapping.dto.AnswerDtoWithTask;
import com.javahelps.jpa.test.dto_mapping.dto.AnswerDtoWithTaskDto;
import com.javahelps.jpa.test.dto_mapping.dto.TaskDtoWithAnswersDto;
import com.javahelps.jpa.test.dto_mapping.model.Answer;
import com.javahelps.jpa.test.dto_mapping.model.Task;
import com.javahelps.jpa.test.util.PersistentHelper;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.stream.Collectors;

public class ComplexMappingSolution {
    public static void main(String[] args) {
        EntityManager entityManager = PersistentHelper.getEntityManager(new Class[]{Answer.class, Task.class});

        Util.saveData(entityManager);
        entityManager.clear();

        {//для того, что бы избежать n+1 проблемы исправим изначально неверную структуру, в которой дто включает
            //в свой состав сущности
            entityManager.getTransaction().begin();

            List<AnswerDtoWithTaskDto> answerDtoWithTasksDto = entityManager.createQuery(
                    "SELECT new com.javahelps.jpa.test.dto_mapping.dto.AnswerDtoWithTaskDto(" +
                            "a.id, " +
                            "a.answer, " +
                            "a.task.id, " +
                            "a.task.title" +
                            ") FROM "+Answer.class.getName()+" a",
                    AnswerDtoWithTaskDto.class
            )
                    .getResultList();

            for (AnswerDtoWithTaskDto answerDtoWithTaskDto : answerDtoWithTasksDto) {
                System.out.println(answerDtoWithTaskDto);
                System.out.println(answerDtoWithTaskDto.getTaskDto());
            }

            entityManager.getTransaction().commit();
        }

        entityManager.clear();

        {//абсолютно такое же решение и для более сложной связи - избавляемся от сущности - избавляемся от n+1 проблемы
            //но у нас по-прежнему в базу отправляется 2 запроса. Чем больше полей придётся собирать подобным образом
            // - тем больше запросов будут отправляться в базу
            List<TaskDtoWithAnswersDto> taskDtoWithAnswers = entityManager.createQuery(
                    "SELECT new com.javahelps.jpa.test.dto_mapping.dto.TaskDtoWithAnswersDto(" +
                            "t.id, " +
                            "t.title " +
                            ") FROM "+Task.class.getName()+" t",
                    TaskDtoWithAnswersDto.class
            )
                    .getResultList();

            List<Long> ids = taskDtoWithAnswers.stream().mapToLong(TaskDtoWithAnswersDto::getId).boxed().collect(Collectors.toList());

            List<AnswerDto> answers = entityManager.createQuery(
                    "SELECT new com.javahelps.jpa.test.dto_mapping.dto.AnswerDto(a.id, a.answer, a.task.id)" +
                            " FROM Answer a WHERE a.task.id IN (:ids)",
                    AnswerDto.class
            )
                    .setParameter("ids", ids)
                    .getResultList();

            taskDtoWithAnswers.forEach(tdwa -> tdwa.setAnswerDtos(
                    answers.stream().filter(a -> a.getTaskId() == tdwa.getId()).collect(Collectors.toList())
            ));

            for (TaskDtoWithAnswersDto taskDtoWithAnswersDto : taskDtoWithAnswers) {
                System.out.println(taskDtoWithAnswers);

                for (AnswerDto answerDto : taskDtoWithAnswersDto.getAnswerDtos()) {
                    System.out.println(answerDto);
                }
            }
        }
    }
}
