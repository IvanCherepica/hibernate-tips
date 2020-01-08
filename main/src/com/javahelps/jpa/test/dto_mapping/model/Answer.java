package com.javahelps.jpa.test.dto_mapping.model;

import com.javahelps.jpa.test.dto_mapping.dto.AnswerDto;
import com.javahelps.jpa.test.dto_mapping.dto.AnswerDtoWithTaskDto;
import com.javahelps.jpa.test.dto_mapping.dto.TaskDto;

import javax.persistence.*;

@Entity
@Table(name = "answer")
@SqlResultSetMappings({
        @SqlResultSetMapping(
                name = "AnswerDtoMapping",
                classes = @ConstructorResult(
                        targetClass = AnswerDto.class,
                        columns = {
                                @ColumnResult(name = "id", type = Long.class),
                                @ColumnResult(name = "answer")})),
        @SqlResultSetMapping(
                name = "AnswerDtoWithTaskDtoMapping",
                classes = {
                        @ConstructorResult(
                                targetClass = AnswerDtoWithTaskDto.class,
                                columns = {
                                        @ColumnResult(name = "answerId", type = Long.class),
                                        @ColumnResult(name = "answer")}),
                        @ConstructorResult(
                                targetClass = TaskDto.class,
                                columns = {
                                        @ColumnResult(name = "taskId", type = Long.class),
                                        @ColumnResult(name = "title")})})
})
public class Answer {
    @Id
    @GeneratedValue
    private long id;

    private String answer;

    @ManyToOne
    @JoinColumn(name = "task_id")
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
                '}';
    }
}
