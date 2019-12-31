package com.javahelps.jpa.test.n_plus_1_problem;

import com.javahelps.jpa.test.util.PersistentHelper;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

public class _30_Problem {
    public static void main(String[] args) {
        EntityManager entityManager = PersistentHelper.getEntityManager(new Class[] {Task.class, Answer.class});

        saveData(entityManager);
        entityManager.clear();
        System.out.println();
        System.out.println("--after saving data--");
        System.out.println();

        entityManager.getTransaction().begin();

        //та же самая n+1 проблема, но уже с нативным запросом. что же делать?
        List<Task> tasks = entityManager.createNativeQuery("SELECT * FROM task", Task.class).getResultList();

        entityManager.getTransaction().commit();

        for (Task task : tasks) {
            System.out.println(task.getTitle());
            task.getAnswers().forEach(a -> System.out.println(a.getAnswer()));
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

        task1.addAnswer(answer1);
        task1.addAnswer(answer2);
        task1.addAnswer(answer3);

        task2.addAnswer(answer4);
        task2.addAnswer(answer5);
        task2.addAnswer(answer6);

        task3.addAnswer(answer7);
        task3.addAnswer(answer8);
        task3.addAnswer(answer9);

        entityManager.getTransaction().commit();
    }


    @Entity
    @Table(name = "task")
    static class Task {
        @Id
        @GeneratedValue
        private long id;

        private String title;

        @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
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

        //заводим специальные методы для добавления и удаления, которые позволяют избавиться от повторного кода
        public void addAnswer(Answer answer) {
            this.answers.add(answer);
            answer.setTask(this);
        }

        public void removeAnswer(Answer answer) {
            this.answers.remove(answer);
            answer.setTask(null);
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
                    '}';
        }
    }

    @Entity
    @Table(name = "answer")
    static class Answer {
        @Id
        @GeneratedValue
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
                    ", answer='" + answer + '\'' +
                    ", task=" + task +
                    '}';
        }
    }
}
