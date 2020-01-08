package com.javahelps.jpa.test.dto_mapping.jpa_sql_result_set_mapping;

import com.javahelps.jpa.test.dto_mapping.Util;
import com.javahelps.jpa.test.dto_mapping.dto.AnswerDto;
import com.javahelps.jpa.test.dto_mapping.dto.AnswerDtoWithTaskDto;
import com.javahelps.jpa.test.dto_mapping.dto.TaskDto;
import com.javahelps.jpa.test.dto_mapping.model.Answer;
import com.javahelps.jpa.test.dto_mapping.model.Task;
import com.javahelps.jpa.test.util.PersistentHelper;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ComplexDtoMapping {
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

            entityManager.  getTransaction().commit();
        }

        entityManager.clear();

        {//то же самое для любой другой дто, в состав которой не входят сущности
            entityManager.getTransaction().begin();

            List<Object> objects = entityManager.createNativeQuery(
                    "SELECT a.id AS answerId, a.answer, t.id AS taskId, title FROM answer a LEFT JOIN task t on a.task_id = t.id",
                    "AnswerDtoWithTaskDtoMapping"
            )
                    .getResultList();

            List<AnswerDtoWithTaskDto> answerDtoWithTaskDtos = new ArrayList<>();

            objects.forEach((record) -> {
                Object[] recordArray = (Object[])record;
                AnswerDtoWithTaskDto answerDtoWithTaskDto = (AnswerDtoWithTaskDto)recordArray[0];
                TaskDto taskDto = (TaskDto) recordArray[1];
                answerDtoWithTaskDto.setTaskDto(taskDto);
                answerDtoWithTaskDtos.add(answerDtoWithTaskDto);
            });

            for (AnswerDtoWithTaskDto answerDtoWithTaskDto : answerDtoWithTaskDtos) {
                System.out.println(answerDtoWithTaskDto);
                System.out.println(answerDtoWithTaskDto.getTaskDto());
            }

            entityManager.getTransaction().commit();
        }

    }
}
