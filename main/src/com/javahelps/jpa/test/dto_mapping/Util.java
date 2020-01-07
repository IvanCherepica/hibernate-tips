package com.javahelps.jpa.test.dto_mapping;

import com.javahelps.jpa.test.dto_mapping.model.Answer;
import com.javahelps.jpa.test.dto_mapping.model.Task;

import javax.persistence.EntityManager;
import javax.swing.text.html.parser.Entity;

public class Util {
    public static void saveData(EntityManager entityManager) {
        entityManager.getTransaction().begin();

        Task task1 = new Task("task_1");
        Task task2 = new Task("task_2");
        Task task3 = new Task("task_3");

        task1.addAnswer(new Answer("answer_1"));
        task1.addAnswer(new Answer("answer_2"));
        task1.addAnswer(new Answer("answer_3"));

        task2.addAnswer(new Answer("answer_4"));
        task2.addAnswer(new Answer("answer_5"));
        task2.addAnswer(new Answer("answer_6"));

        task3.addAnswer(new Answer("answer_7"));
        task3.addAnswer(new Answer("answer_8"));
        task3.addAnswer(new Answer("answer_9"));

        entityManager.persist(task1);
        entityManager.persist(task2);
        entityManager.persist(task3);

        entityManager.getTransaction().commit();
    }
}
