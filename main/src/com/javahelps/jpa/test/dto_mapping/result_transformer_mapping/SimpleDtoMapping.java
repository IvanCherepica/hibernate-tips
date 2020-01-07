package com.javahelps.jpa.test.dto_mapping.result_transformer_mapping;

import com.javahelps.jpa.test.dto_mapping.Util;
import com.javahelps.jpa.test.dto_mapping.dto.AnswerDto;
import com.javahelps.jpa.test.dto_mapping.dto.TaskDto;
import com.javahelps.jpa.test.dto_mapping.model.Answer;
import com.javahelps.jpa.test.dto_mapping.model.Task;
import com.javahelps.jpa.test.util.PersistentHelper;
import org.hibernate.Query;
import org.hibernate.transform.ResultTransformer;

import javax.persistence.EntityManager;
import java.util.List;

public class SimpleDtoMapping {
    public static void main(String[] args) {
        EntityManager entityManager = PersistentHelper.getEntityManager(new Class[]{Answer.class, Task.class});

        Util.saveData(entityManager);
        entityManager.clear();

        {//крайне громоздкая конструкция. сам по себе метод setResultTransformer является устаревшим и
            //текущая версия хибернейта не поддерживает использование функциолнальных интерфейсов, поэтому
            //приходится писать крайне много избыточного кода
            entityManager.getTransaction().begin();

            List<TaskDto> taskDtos = entityManager.createQuery(
                    "SELECT t.id, t.title FROM Task t"
            )
                    .unwrap(Query.class)
                    .setResultTransformer(
                            new ResultTransformer() {
                                @Override
                                public Object transformTuple(
                                        Object[] tuple,
                                        String[] aliases) {
                                    return new TaskDto(
                                            (Long) tuple[0],
                                            (String) tuple[1]
                                    );
                                }

                                @Override
                                public List transformList(List collection) {
                                    return collection;
                                }
                            }
                    ).list();

            taskDtos.forEach(System.out::println);

            entityManager.getTransaction().commit();
        }

        entityManager.clear();

        {//то же самое для любой другой дто, в состав которой не входят сущности. использоватье constructor expression
            //из jpa выглядет куда более привлекательным
            entityManager.getTransaction().begin();

            List<AnswerDto> answerDtos = entityManager.createQuery(
                    "SELECT a.id, a.answer FROM Answer a"
            )
                    .unwrap(Query.class)
                    .setResultTransformer(
                            new ResultTransformer() {
                                @Override
                                public Object transformTuple(
                                        Object[] tuple,
                                        String[] aliases) {
                                    return new AnswerDto(
                                            (Long) tuple[0],
                                            (String) tuple[1]
                                    );
                                }

                                @Override
                                public List transformList(List collection) {
                                    return collection;
                                }
                            }
                    ).list();

            answerDtos.forEach(System.out::println);

            entityManager.getTransaction().commit();
        }

    }

}
