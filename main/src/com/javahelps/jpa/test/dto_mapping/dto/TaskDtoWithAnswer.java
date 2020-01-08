package com.javahelps.jpa.test.dto_mapping.dto;

import com.javahelps.jpa.test.dto_mapping.model.Answer;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class TaskDtoWithAnswer {
    private long id;

    private String title;

    private List<Answer> answers;

    public TaskDtoWithAnswer() {
    }

    public TaskDtoWithAnswer(long id, String title) {
        this.id = id;
        this.title = title;
    }

    public TaskDtoWithAnswer(long id, String title, List<Answer> answers) {
        this.id = id;
        this.title = title;
        this.answers = answers;
    }

//    public TaskDtoWithAnswer(long id, String title, List<Answer> object) {
//        this.id = id;
//        this.title = title;
//        this.answers = (List<Answer>) object;
//    }

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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaskDtoWithAnswer that = (TaskDtoWithAnswer) o;
        return id == that.id &&
                Objects.equals(title, that.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title);
    }

    @Override
    public String toString() {
        return "TaskDtoWithAnswer{" +
                "id=" + id +
                ", title='" + title + '\'' +
                '}';
    }
}
