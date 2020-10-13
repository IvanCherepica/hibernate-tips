package com.javahelps.jpa.test.element_list;

import com.vladmihalcea.hibernate.type.array.ListArrayType;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import java.util.ArrayList;
import java.util.List;


class MorphemeDto {
    private Long id;

    private List<String> morphemes;

    public MorphemeDto() {
    }

    public MorphemeDto(Long id) {
            this.id = id;
    }

    public MorphemeDto(Long id, Object morphemes) {
        this.id = id;
        this.morphemes = (List<String>) morphemes;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<String> getMorphemes() {
        return morphemes;
    }

    public void setMorphemes(List<String> morphemes) {
            this.morphemes = morphemes;
        }
}