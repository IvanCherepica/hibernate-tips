package com.javahelps.jpa.test.dto_mapping.dto;

import com.javahelps.jpa.test.dto_mapping.model.Task;

public class AnswerDtoWithTaskDto {
    private long id;

    private String answer;

    private TaskDto taskDto;

    public AnswerDtoWithTaskDto() {
    }

    public AnswerDtoWithTaskDto(long id, String answer) {
        this.id = id;
        this.answer = answer;
    }

    public AnswerDtoWithTaskDto(long id, String answer, TaskDto taskDto) {
        this.id = id;
        this.answer = answer;
        this.taskDto = taskDto;
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

    public TaskDto getTaskDto() {
        return taskDto;
    }

    public void setTaskDto(TaskDto taskDto) {
        this.taskDto = taskDto;
    }

    @Override
    public String toString() {
        return "AnswerDtoWithTaskDto{" +
                "id=" + id +
                ", answer='" + answer + '\'' +
                '}';
    }
}
