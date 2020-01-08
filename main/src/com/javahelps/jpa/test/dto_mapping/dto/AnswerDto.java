package com.javahelps.jpa.test.dto_mapping.dto;

import java.util.Objects;

public class AnswerDto {
    private long id;

    private String answer;

    private long taskId;

    public AnswerDto() {
    }

    public AnswerDto(long id, String answer) {
        this.id = id;
        this.answer = answer;
    }

    public AnswerDto(long id, String answer, long taskId) {
        this.id = id;
        this.answer = answer;
        this.taskId = taskId;
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

    public long getTaskId() {
        return taskId;
    }

    public void setTaskId(long taskId) {
        this.taskId = taskId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AnswerDto answerDto = (AnswerDto) o;
        return id == answerDto.id &&
                Objects.equals(answer, answerDto.answer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, answer);
    }

    @Override
    public String toString() {
        return "AnswerDto{" +
                "id=" + id +
                ", answer='" + answer + '\'' +
                '}';
    }
}
