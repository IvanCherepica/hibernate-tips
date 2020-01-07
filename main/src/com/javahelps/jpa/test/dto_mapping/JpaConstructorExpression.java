package com.javahelps.jpa.test.dto_mapping;

import com.javahelps.jpa.test.dto_mapping.dto.AnswerDto;
import com.javahelps.jpa.test.dto_mapping.dto.AnswerDtoWithTask;
import com.javahelps.jpa.test.dto_mapping.dto.TaskDto;
import com.javahelps.jpa.test.dto_mapping.model.Answer;
import com.javahelps.jpa.test.dto_mapping.model.Task;
import com.javahelps.jpa.test.util.PersistentHelper;

import javax.persistence.EntityManager;
import java.util.List;

public class JpaConstructorExpression {
    public static void main(String[] args) {
        EntityManager entityManager = PersistentHelper.getEntityManager(new Class[]{Answer.class, Task.class});

        Util.saveData(entityManager);
        entityManager.clear();

        {//довольно простой маппинг, получаем дто без явных кастов, ручных маппингов и лишних аннотаций
            List<TaskDto> taskDtos = entityManager.createQuery(
                    "SELECT new com.javahelps.jpa.test.dto_mapping.dto.TaskDto(t.id, t.title) FROM "+Task.class.getName()+" t",
                    TaskDto.class
            )
                    .getResultList();

            taskDtos.forEach(System.out::println);
        }

        entityManager.clear();

        {//то же самое для любой другой дто, в состав которой не входят сущности
            List<AnswerDto> answerDtos = entityManager.createQuery(
                    "SELECT new com.javahelps.jpa.test.dto_mapping.dto.AnswerDto(a.id, a.answer) FROM "+Answer.class.getName()+" a",
                    AnswerDto.class
            )
                    .getResultList();

            answerDtos.forEach(System.out::println);
        }

        entityManager.clear();

        {
            List<AnswerDtoWithTask> answerDtoWithTasks = entityManager.createQuery(
                    "SELECT new com.javahelps.jpa.test.dto_mapping.dto.AnswerDtoWithTask(" +
                            "a.id, " +
                            "a.answer, " +
                            "t" +
                            ") FROM "+Answer.class.getName()+" a JOIN a.task t",
                    AnswerDtoWithTask.class
            )
                    .getResultList();

            for (AnswerDtoWithTask answerDtoWithTask : answerDtoWithTasks) {
                System.out.println(answerDtoWithTask);
                System.out.println(answerDtoWithTask.getAnswer());
            }
        }
    }
}
