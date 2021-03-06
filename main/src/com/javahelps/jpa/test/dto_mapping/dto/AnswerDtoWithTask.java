package com.javahelps.jpa.test.dto_mapping.dto;

import com.javahelps.jpa.test.dto_mapping.model.Task;

import java.util.Objects;

public class AnswerDtoWithTask {
    private long id;

    private String answer;

    private Task task;

    public AnswerDtoWithTask() {
    }

    public AnswerDtoWithTask(long id, String answer) {
        this.id = id;
        this.answer = answer;
    }

    public AnswerDtoWithTask(long id, String answer, Task task) {
        this.id = id;
        this.answer = answer;
        this.task = task;
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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AnswerDtoWithTask that = (AnswerDtoWithTask) o;
        return id == that.id &&
                Objects.equals(answer, that.answer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, answer);
    }

    @Override
    public String toString() {
        return "AnswerDtoWithTask{" +
                "id=" + id +
                ", answer='" + answer + '\'' +
                '}';
    }
}
