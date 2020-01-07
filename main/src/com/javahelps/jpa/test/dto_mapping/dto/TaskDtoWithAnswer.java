package com.javahelps.jpa.test.dto_mapping.dto;

import com.javahelps.jpa.test.dto_mapping.model.Answer;

import java.util.Collection;
import java.util.List;

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
    public String toString() {
        return "TaskDtoWithAnswer{" +
                "id=" + id +
                ", title='" + title + '\'' +
                '}';
    }
}
