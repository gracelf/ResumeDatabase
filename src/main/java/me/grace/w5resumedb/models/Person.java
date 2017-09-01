package me.grace.w5resumedb.models;

import org.hibernate.validator.constraints.Email;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;


@Entity
public class Person {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long uuid;

    @NotNull
    @Size(min=2)
    private String firstName;


    @NotNull
    @Size(min=2)
    private String lastName;

    @Email
    private String email;


    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL, fetch=FetchType.EAGER)
    private Set<Education> educations;

    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL, fetch=FetchType.EAGER)
    private Set<Skill> skills;

    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL, fetch=FetchType.EAGER)
    private Set<Experience> experiences;


    //add a course list many to many relationship
    @ManyToMany(mappedBy = "students")
    private Set<Course> courselist;


    public Set<Course> getCourselist() {
        return courselist;
    }

    public void setCourselist(Set<Course> courselist) {
        this.courselist = courselist;
    }


    public long getUuid() {
        return uuid;
    }

    public void setUuid(long uuid) {
        this.uuid = uuid;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }



    public Set<Education> getEducations() {
        return educations;
    }

    public void setEducations(Set<Education> educations) {
        this.educations = educations;
    }

    public Set<Skill> getSkills() {
        return skills;
    }

    public void setSkills(Set<Skill> skills) {
        this.skills = skills;
    }

    public Set<Experience> getExperiences() {
        return experiences;
    }

    public void setExperiences(Set<Experience> experiences) {
        this.experiences = experiences;
    }

    //constructor for person, initialize an empty set of education, skill and exp
    public Person(){
        this.educations= new HashSet<Education>();
        this.skills=new HashSet<Skill>();
        this.experiences=new HashSet<Experience>();
        this.courselist = new HashSet<Course>();
    }

    //add course to this person
    public void addCoursetoperson(Course c)
    {
        this.courselist.add(c);
    }

    //create add method for education, skill and exp
    public void addEdu(Education e)
    {
        e.setPerson(this);
        this.educations.add(e);
    }

    public void removeEdu(Education e)
    {
        this.educations.remove(e);
    }


    public void addSkl(Skill s)
    {
        s.setPerson(this);
        this.skills.add(s);
    }

    public void removeSkl(Skill s)
    {
        this.skills.remove(s);
    }

    public void addExp(Experience ex)
    {
        ex.setPerson(this);
        this.experiences.add(ex);
    }

    public void removeExp(Experience ex)
    {
        this.experiences.remove(ex);
    }

}
