package me.grace.w5resumedb.models;


import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long courseId;


    // the courseCode is mandatory, other fields are optional
    @NotEmpty
    private String courseCode;

    private String instructor;

    @NotNull
    private double credit;

    private String description;

    @ManyToMany
    private Set<Person> students;




    public long getCourseId() {
        return courseId;
    }

    public void setCourseId(long courseId) {
        this.courseId = courseId;
    }

    public Set<Person> getStudents() {
        return students;
    }

    public void setStudents(Set<Person> students) {
        this.students = students;
    }


    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getInstructor() {
        return instructor;
    }

    public void setInstructor(String instructor) {
        this.instructor = instructor;
    }

    public double getCredit() {
        return credit;
    }

    public void setCredit(double credit) {
        this.credit = credit;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    //constructor for course
    public Course()
    {
        this.students = new HashSet<Person>();
    }


    // define method for adding and removing student from course
    public void addPersontoCourse(Person p)
    {
        this.students.add(p);
    }

    public void removePersonFromCourse(Person p)  {
        this.students.remove(p);
    }

}
