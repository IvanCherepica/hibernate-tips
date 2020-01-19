package com.javahelps.jpa.test.one_to_many.bidirectional;

import com.javahelps.jpa.test.util.PersistentHelper;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

public class _5_JoinColumnSolution {
    public static void main(String[] args) {
        EntityManager entityManager = PersistentHelper.getEntityManager(new Class[] {Task.class, Answer.class});

        saveTaskWithAnswers(entityManager);

        //начинаем новую транзакцию
        entityManager.getTransaction().begin();

        //достём из базы задачу с id = 1
        Task task = entityManager.find(Task.class, 1L);

        entityManager.getTransaction().commit();

        //проблема: как мы видим task во всех трёх сущностях null. где же тогда сохранились связи?
        //связи сохранились в таблице task_answer, которую хибернейт создал самостоятельно, для организации many2one
        System.out.println(task.getAnswers());


        entityManager.getTransaction().begin();

        Answer answer = entityManager.find(Answer.class, 1L);

        entityManager.getTransaction().commit();

        //от дополнительной таблицы мы избавились, однако, это не улучшило картину с персистентностью данных
        //как и прежде - мы не имеем никакой возможности сохранить наши связи в бау
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

        //решение первой проблемы в классе _1_Problem1 заключается в том, что бы первести все обхекты answer в состояние persistent
        entityManager.persist(answer1);
        entityManager.persist(answer2);
        entityManager.persist(answer3);

        //вносим изменения в сущность task, в тот момент, когда она ещё transient и не прикреплена к сессии
        task.getAnswers().add(answer1);
        task.getAnswers().add(answer2);
        task.getAnswers().add(answer3);

        //переводим объект task в состояние persist
        entityManager.persist(task);

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
