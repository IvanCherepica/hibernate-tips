package com.javahelps.jpa.test.n_plus_1_problem_many_to_one.join_fetch_solution;

import com.javahelps.jpa.test.util.PersistentHelper;

import javax.persistence.*;
import java.util.List;

public class _1_Problem {
    public static void main(String[] args) {
        EntityManager entityManager = PersistentHelper.getEntityManager(new Class[] {Task.class, Answer.class});

        saveData(entityManager);
        entityManager.clear();
        System.out.println();
        System.out.println("--after saving data--");
        System.out.println();

        entityManager.getTransaction().begin();

        List<Answer> answers = entityManager.createQuery("FROM " + Answer.class.getName(), Answer.class).getResultList();

        entityManager.getTransaction().commit();

        //получаем один дополнительный запрос на каждый answer
        for (Answer answer : answers) {
            System.out.println(answer.getAnswer());
            System.out.println(answer.getTask());
        }
    }

    private static void saveData(EntityManager entityManager) {
        entityManager.getTransaction().begin();

        Task task1 = new Task("Task 1");
        Task task2 = new Task("Task 2");
        Task task3 = new Task("Task 3");

        Answer answer1 = new Answer("Answer1");
        Answer answer2 = new Answer("Answer2");
        Answer answer3 = new Answer("Answer3");

        Answer answer4 = new Answer("Answer4");
        Answer answer5 = new Answer("Answer5");
        Answer answer6 = new Answer("Answer6");

        Answer answer7 = new Answer("Answer7");
        Answer answer8 = new Answer("Answer8");
        Answer answer9 = new Answer("Answer9");

        entityManager.persist(task1);
        entityManager.persist(task2);
        entityManager.persist(task3);

        entityManager.persist(answer1);
        entityManager.persist(answer2);
        entityManager.persist(answer3);
        entityManager.persist(answer4);
        entityManager.persist(answer5);
        entityManager.persist(answer6);
        entityManager.persist(answer7);
        entityManager.persist(answer8);
        entityManager.persist(answer9);

        answer1.setTask(task1);
        answer2.setTask(task1);
        answer3.setTask(task1);

        answer4.setTask(task2);
        answer5.setTask(task2);
        answer6.setTask(task2);

        answer7.setTask(task3);
        answer8.setTask(task3);
        answer9.setTask(task3);

        entityManager.getTransaction().commit();
    }


    @Entity
    @Table(name = "task")
    private static class Task {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private long id;

        private String title;

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

        @Override
        public String toString() {
            return "Task{" +
                    "id=" + id +
                    ", title='" + title + '\'' +
                    '}';
        }
    }

    @Entity
    @Table(name = "answer")
    private static class Answer {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private long id;

        private String answer;

        @ManyToOne(fetch = FetchType.LAZY)
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
                    ", task=" + task +
                    '}';
        }
    }
}
