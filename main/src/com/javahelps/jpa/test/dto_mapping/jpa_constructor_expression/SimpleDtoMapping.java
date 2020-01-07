package com.javahelps.jpa.test.dto_mapping.jpa_constructor_expression;

import com.javahelps.jpa.test.dto_mapping.Util;
import com.javahelps.jpa.test.dto_mapping.dto.AnswerDto;
import com.javahelps.jpa.test.dto_mapping.dto.TaskDto;
import com.javahelps.jpa.test.dto_mapping.model.Answer;
import com.javahelps.jpa.test.dto_mapping.model.Task;
import com.javahelps.jpa.test.util.PersistentHelper;

import javax.persistence.EntityManager;
import java.util.List;

public class SimpleDtoMapping {
    public static void main(String[] args) {
        EntityManager entityManager = PersistentHelper.getEntityManager(new Class[]{Answer.class, Task.class});

        Util.saveData(entityManager);
        entityManager.clear();

        {//довольно простой маппинг, получаем дто без явных кастов, ручных маппингов и лишних аннотаций
            entityManager.getTransaction().begin();

            List<TaskDto> taskDtos = entityManager.createQuery(
                    "SELECT new com.javahelps.jpa.test.dto_mapping.dto.TaskDto(t.id, t.title) FROM "+Task.class.getName()+" t",
                    TaskDto.class
            )
                    .getResultList();

            taskDtos.forEach(System.out::println);

            entityManager.getTransaction().commit();
        }

        entityManager.clear();

        {//то же самое для любой другой дто, в состав которой не входят сущности
            entityManager.getTransaction().begin();

            List<AnswerDto> answerDtos = entityManager.createQuery(
                    "SELECT new com.javahelps.jpa.test.dto_mapping.dto.AnswerDto(a.id, a.answer) FROM "+Answer.class.getName()+" a",
                    AnswerDto.class
            )
                    .getResultList();

            answerDtos.forEach(System.out::println);

            entityManager.getTransaction().commit();
        }

    }
}
