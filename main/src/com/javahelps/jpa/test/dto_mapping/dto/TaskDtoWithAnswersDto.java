package com.javahelps.jpa.test.dto_mapping.dto;

import com.javahelps.jpa.test.dto_mapping.model.Answer;

import java.util.List;
import java.util.Objects;

public class TaskDtoWithAnswersDto {
    private long id;

    private String title;

    private List<AnswerDto> answerDtos;

    public TaskDtoWithAnswersDto() {
    }

    public TaskDtoWithAnswersDto(long id, String title) {
        this.id = id;
        this.title = title;
    }

    public TaskDtoWithAnswersDto(long id, String title, List<AnswerDto> answerDtos) {
        this.id = id;
        this.title = title;
        this.answerDtos = answerDtos;
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

    public List<AnswerDto> getAnswerDtos() {
        return answerDtos;
    }

    public void setAnswerDtos(List<AnswerDto> answerDtos) {
        this.answerDtos = answerDtos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TaskDtoWithAnswersDto that = (TaskDtoWithAnswersDto) o;
        return id == that.id &&
                Objects.equals(title, that.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title);
    }

    @Override
    public String toString() {
        return "TaskDtoWithAnswersDto{" +
                "id=" + id +
                ", title='" + title + '\'' +
                '}';
    }
}
