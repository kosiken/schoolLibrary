package org.allisonkosy.entity;
import org.allisonkosy.runner.Library;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "students")
public class Student implements Serializable, Model {
   public Student(String name, int age) {
        this.name = name;
        this.age = age;
    }
    public Student() {

    }
    @Id
    @GeneratedValue
    private Long id;


    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "age", nullable = false)
    private Integer age;

    public void setAge(Integer age) {
        this.age = age;
    }

    public Integer getAge() {
        return age;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getName() {
        return name;
    }

    public Long getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", age=" + age +
                ", name='" + name + '\'' +
                '}';
    }


    @Override
    public String getModelName() {
        return modelName();
    }


    public static String modelName() {
        return "Student";
    }

    public static String printIdentifier() {
        return "FROM Student s";
    }


}
