package com.javahelps.jpa.test.one_to_many.bidirectional;

import com.javahelps.jpa.test.util.PersistentHelper;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

public class _2_Solution1 {
    public static void main(String[] args) {
        EntityManager entityManager = PersistentHelper.getEntityManager(new Class[] {Task.class, Answer.class});

        entityManager.getTransaction().begin();

        //создаём три ответа; состояние объектов - transient
        Answer answer1 = new Answer("Answer 1");
        Answer answer2 = new Answer("Answer 2");
        Answer answer3 = new Answer("Answer 3");

        //создаём задачу, тоже transient
        Task task = new Task("Task 1");

        //решение первой проблемы в классе _1_Problem1 заключается в том, что бы первести все обхекты answer в состояние persistent
        entityManager.persist(answer1);
        entityManager.persist(answer2);
        entityManager.persist(answer3);

        //вносим изменения в сущность task, в тот момент, когда она ещё transient и не прикреплена к сессии
        task.getAnswers().add(answer1);
        task.getAnswers().add(answer2);
        task.getAnswers().add(answer3);

        //переводим объект task в состояние prsist
        entityManager.persist(task);

        //Теперь всё хорошо, транзакция комитится - все изменения заходят в базу
        entityManager.getTransaction().commit();
    }

    @Entity
    @Table(name = "task")
    static class Task {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private long id;

        private String title;

        @OneToMany
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
