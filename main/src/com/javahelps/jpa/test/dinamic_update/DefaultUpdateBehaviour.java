package com.javahelps.jpa.test.dinamic_update;


import com.javahelps.jpa.test.util.PersistentHelper;

import javax.persistence.*;
import java.util.Objects;

public class DefaultUpdateBehaviour {

    private static EntityManager entityManager = PersistentHelper.getEntityManager(new Class[]{Report.class});

    public static void main(String[] args) {
        Report report = new Report("Eric Erickson", "development", 7, true);

        entityManager.getTransaction().begin();
        entityManager.persist(report);
        report.setHasApproved(false);
        report.setEmployeeCount(8);
        entityManager.getTransaction().commit();
    }


    @Entity
    private static class Report {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String reporter;

        private String department;

        private Integer employeeCount;

        private Boolean hasApproved;

        public Report() {
        }

        public Report(String reporter, String department, Integer employeeCount, Boolean hasApproved) {
            this.reporter = reporter;
            this.department = department;
            this.employeeCount = employeeCount;
            this.hasApproved = hasApproved;
        }

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getReporter() {
            return reporter;
        }

        public void setReporter(String reporter) {
            this.reporter = reporter;
        }

        public String getDepartment() {
            return department;
        }

        public void setDepartment(String department) {
            this.department = department;
        }

        public Integer getEmployeeCount() {
            return employeeCount;
        }

        public void setEmployeeCount(Integer employeeCount) {
            this.employeeCount = employeeCount;
        }

        public Boolean getHasApproved() {
            return hasApproved;
        }

        public void setHasApproved(Boolean hasApproved) {
            this.hasApproved = hasApproved;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Report report = (Report) o;
            return Objects.equals(id, report.id) &&
                    Objects.equals(reporter, report.reporter) &&
                    Objects.equals(department, report.department) &&
                    Objects.equals(employeeCount, report.employeeCount) &&
                    Objects.equals(hasApproved, report.hasApproved);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id, reporter, department, employeeCount, hasApproved);
        }
    }
}
