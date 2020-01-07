package com.javahelps.jpa.test.dto_mapping.dto;

public class AnswerDto {
    private long id;

    private String answer;

    public AnswerDto() {
    }

    public AnswerDto(long id, String answer) {
        this.id = id;
        this.answer = answer;
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

    @Override
    public String toString() {
        return "AnswerDto{" +
                "id=" + id +
                ", answer='" + answer + '\'' +
                '}';
    }
}
