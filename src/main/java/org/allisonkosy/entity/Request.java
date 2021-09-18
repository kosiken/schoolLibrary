package org.allisonkosy.entity;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "requests")
public class Request implements Serializable, Model {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @ManyToOne( optional = false)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    public Student getStudent() {
        return student;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBookName() {
        return book.getName();
    }
    @Override
    public String getModelName() {
        return modelName();
    }


    public static String modelName() {
        return "Request";
    }

    @Override
    public String toString() {
        return "Request{" +
                "id=" + id +
                ", book=" + book +
                ", student=" + student +
                '}';
    }

    public static String printIdentifier() {
        return "FROM Request r";
    }
}
