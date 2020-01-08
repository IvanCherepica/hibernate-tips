package com.javahelps.jpa.test.dto_mapping.jpa_sql_result_set_mapping;

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

        {//используя SqlResultSetMapping мы явно привязываемся к нативным запросам
            entityManager.getTransaction().begin();

            List<TaskDto> taskDtos = entityManager.createNativeQuery(
                    "SELECT t.id, t.title FROM task t",
                    "TaskDtoMapping"
            )
                    .getResultList();

            taskDtos.forEach(System.out::println);

            entityManager.getTransaction().commit();
        }

        entityManager.clear();

        {//то же самое для любой другой дто, в состав которой не входят сущности
            entityManager.getTransaction().begin();

            List<AnswerDto> answerDtos = entityManager.createNativeQuery(
                    "SELECT a.id, a.answer FROM answer a",
                    "AnswerDtoMapping"
            )
                    .getResultList();

            answerDtos.forEach(System.out::println);

            entityManager.getTransaction().commit();
        }

    }

}
