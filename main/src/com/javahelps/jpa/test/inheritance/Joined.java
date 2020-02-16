package com.javahelps.jpa.test.inheritance;

import com.javahelps.jpa.test.util.PersistentHelper;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Joined {
    public static void main(String[] args) {
        EntityManager entityManager = PersistentHelper.getEntityManager(new Class[] {Department.class, Employee.class, Manager.class, Expert.class});

        saveData(entityManager);
        entityManager.clear();

        {   //можем использовать полиморфные запросы

            System.out.println();
            System.out.println("Before Employee select");
            System.out.println();

            entityManager.getTransaction().begin();

            List<Employee> employees =
                    entityManager.createQuery("FROM " + Employee.class.getName(), Employee.class).getResultList();

            employees.forEach(System.out::println);

            entityManager.getTransaction().commit();

            System.out.println();
            System.out.println("After Employee select");
            System.out.println();

        }

        entityManager.clear();

        {   //join fetch одного подтипа к другому

            System.out.println();
            System.out.println("Before Manager select");
            System.out.println();

            entityManager.getTransaction().begin();

            List<Manager> employees =
                    entityManager.createQuery("FROM " + Manager.class.getName() + " m JOIN FETCH m.experts", Manager.class).getResultList();

            employees.forEach(System.out::println);

            entityManager.getTransaction().commit();

            System.out.println();
            System.out.println("After Manager select");
            System.out.println();

        }

        entityManager.clear();

        {   //т.к. тип соединения внутренний - мы получим 2 department. фиксится использованием distinct
            System.out.println();
            System.out.println("Before Department select");
            System.out.println();

            entityManager.getTransaction().begin();

            List<Department> employees =
                    entityManager.createQuery("SELECT DISTINCT d FROM " + Department.class.getName() + " d JOIN FETCH d.employees", Department.class).getResultList();

            employees.forEach(System.out::println);

            entityManager.getTransaction().commit();

            System.out.println();
            System.out.println("After Department select");
            System.out.println();
        }

    }

    private static void saveData(EntityManager entityManager) {
        entityManager.getTransaction().begin();

        Department department = new Department("department 1", "Russia");

        Manager manager1 = new Manager("manager 1", 100);
        Manager manager2 = new Manager("manager 2", 120);

        Expert expert1 = new Expert("expert 1", 50);
        Expert expert2 = new Expert("expert 1", 110);
        Expert expert3 = new Expert("expert 1", 170);
        Expert expert4 = new Expert("expert 1", 210);

        manager1.addExpert(expert1);
        manager1.addExpert(expert2);
        manager2.addExpert(expert3);
        manager2.addExpert(expert4);

        department.addEmployee(manager1);
        department.addEmployee(manager2);

        department.addEmployee(expert1);
        department.addEmployee(expert2);
        department.addEmployee(expert3);
        department.addEmployee(expert4);

        entityManager.persist(department);

        entityManager.getTransaction().commit();
    }

    @Entity
    @Table(name = "department")
    static class Department {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String name;

        private String country;

        @OneToMany(mappedBy = "department", cascade = CascadeType.ALL, orphanRemoval = true)
        private List<Employee> employees = new ArrayList<>();

        public Department() {
        }

        public Department(String name, String country) {
            this.name = name;
            this.country = country;
        }

        //заводим специальные методы для добавления и удаления, которые позволяют избавиться от повторного кода
        public void addEmployee(Employee employee) {
            this.employees.add(employee);
            employee.setDepartment(this);
        }

        public void removeEmployee(Expert expert) {
            this.employees.remove(expert);
            expert.setManager(null);
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

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public List<Employee> getEmployees() {
            return employees;
        }

        public void setEmployees(List<Employee> employees) {
            this.employees = employees;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Department that = (Department) o;
            return Objects.equals(id, that.id) &&
                    Objects.equals(name, that.name) &&
                    Objects.equals(country, that.country);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id, name, country);
        }

        @Override
        public String toString() {
            return "Department{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", country='" + country + '\'' +
                    '}';
        }
    }

    @Entity
    @Table(name = "employee")
    @Inheritance(strategy = InheritanceType.JOINED)
    static abstract class Employee {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String name;

        private Integer salary;

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "department_id")
        private Department department;

        public Employee(String name, Integer salary) {
            this.name = name;
            this.salary = salary;
        }

        public Employee() {
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

        public Integer getSalary() {
            return salary;
        }

        public void setSalary(Integer salary) {
            this.salary = salary;
        }

        public Department getDepartment() {
            return department;
        }

        public void setDepartment(Department department) {
            this.department = department;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Employee employee = (Employee) o;
            return Objects.equals(id, employee.id) &&
                    Objects.equals(name, employee.name) &&
                    Objects.equals(salary, employee.salary);
        }

        @Override
        public int hashCode() {
            return Objects.hash(id, name, salary);
        }

        @Override
        public String toString() {
            return "Employee{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", salary=" + salary +
                    '}';
        }
    }

    @Entity
    static class Manager extends Employee {

        @OneToMany(mappedBy = "manager", cascade = CascadeType.ALL, orphanRemoval = true)
        private List<Expert> experts = new ArrayList<>();

        public Manager() {
            super();
        }

        public Manager(String name, Integer salary) {
            super(name, salary);
        }

        //заводим специальные методы для добавления и удаления, которые позволяют избавиться от повторного кода
        public void addExpert(Expert expert) {
            this.experts.add(expert);
            expert.setManager(this);
        }

        public void removeExpert(Expert expert) {
            this.experts.remove(expert);
            expert.setManager(null);
        }

        public List<Expert> getExperts() {
            return experts;
        }

        public void setExperts(List<Expert> experts) {
            this.experts = experts;
        }
    }

    @Entity
    static class Expert extends Employee {

        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "manager_id")
        private Manager manager;

        public Expert() {
            super();
        }

        public Expert(String name, Integer salary) {
            super(name, salary);
        }

        public Manager getManager() {
            return manager;
        }

        public void setManager(Manager manager) {
            this.manager = manager;
        }

    }
}
