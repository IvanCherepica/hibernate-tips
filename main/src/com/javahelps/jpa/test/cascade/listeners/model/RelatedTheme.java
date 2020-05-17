package com.javahelps.jpa.test.cascade.listeners.model;

import javax.persistence.*;

@Entity
@Table(name = "related_theme")
public class RelatedTheme {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String name;

        public RelatedTheme() {
        }

        public RelatedTheme(String name) {
            this.name = name;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof PostComment)) return false;
            return id != null && id.equals(((PostComment) o).getId());
        }

        @Override
        public int hashCode() {
            return 31;
        }
    }