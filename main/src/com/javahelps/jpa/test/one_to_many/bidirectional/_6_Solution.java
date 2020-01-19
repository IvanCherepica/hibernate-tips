package com.javahelps.jpa.test.one_to_many.bidirectional;

import com.javahelps.jpa.test.util.PersistentHelper;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

public class _6_Solution {
    public static void main(String[] args) {
        EntityManager entityManager = PersistentHelper.getEntityManager(new Class[] {Task.class, Answer.class});

        saveTaskWithAnswers(entityManager);

        //начинаем новую транзакцию
        entityManager.getTransaction().begin();

        //достём из базы задачу с id = 1
        Task task = entityManager.find(Task.class, 1L);

        entityManager.getTransaction().commit();

        //теперь всё работает
        System.out.println(task.getAnswers());


        entityManager.getTransaction().begin();

        Answer answer = entityManager.find(Answer.class, 1L);

        entityManager.getTransaction().commit();

        //и здесь
        System.out.println(answer.getTask());

    }

    private static void saveTaskWithAnswers(EntityManager entityManager) {
        entityManager.getTransaction().begin();

        //создаём три ответа; состояние объектов - transient
        Answer answer1 = new Answer("Answer 1");
        Answer answer2 = new Answer("Answer 2");
        Answer answer3 = new Answer("Answer 3");

        //создаём задачу, тоже transient
        Task task = new Task("Task 1");

        //сначала сохраняем в базу объект task, что бы избежать ошибки переходного состояния
        entityManager.persist(task);

        //после этого persistent объект присоединяем к transient объектам, прежде чем сохранить их в базу
        //остааётся только одна проблема: такой код выглядит сложным и запутанным. не хотелось бы каждый раз его писать
        answer1.setTask(task);
        answer2.setTask(task);
        answer3.setTask(task);

        entityManager.persist(answer1);
        entityManager.persist(answer2);
        entityManager.persist(answer3);

        entityManager.getTransaction().commit();
    }

    @Entity
    @Table(name = "task")
    static class Task {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private long id;

        private String title;

        @OneToMany(mappedBy = "task")
        private List<Answer> answers = new ArrayList<>();

        public Task(String title) {
            this.title = title;
        }

        public Task() {
        }

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public List<Answer> getAnswers() {
            return answers;
        }

        public void setAnswers(List<Answer> answers) {
            this.answers = answers;
        }

        @Override
        public String toString() {
            return "Task{" +
                    "id=" + id +
                    ", title='" + title + '\'' +
                    ", answers=" + answers +
                    '}';
        }
    }

    @Entity
    @Table(name = "answer")
    static class Answer {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private long id;

        private String answer;

        @ManyToOne
        @JoinColumn(name = "answer_id")
        private Task task;

        public Answer(String answer) {
            this.answer = answer;
        }

        public Answer() {
        }

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public String getAnswer() {
            return answer;
        }

        public void setAnswer(String answer) {
            this.answer = answer;
        }

        public Task getTask() {
            return task;
        }

        public void setTask(Task task) {
            this.task = task;
        }

        @Override
        public String toString() {
            return "Answer{" +
                    "id=" + id +
                    ", answer='" + answer + '\'' +
                    ", task=" + task +
                    '}';
        }
    }
}
