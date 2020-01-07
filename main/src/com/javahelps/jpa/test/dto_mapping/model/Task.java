package com.javahelps.jpa.test.dto_mapping.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "task")
public class Task {
    @Id
    @GeneratedValue
    private long id;

    private String title;

    @OneToMany(mappedBy = "task", orphanRemoval = true, cascade = CascadeType.ALL)
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
