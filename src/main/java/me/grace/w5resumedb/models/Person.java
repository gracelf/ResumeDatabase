package me.grace.w5resumedb.models;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;


@Entity
public class Person {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long uuid;

    @NotEmpty
    private String firstName;

    @NotEmpty
    private String lastName;

    @NotEmpty
    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    private boolean enabled;

    @Email
    @NotEmpty
    private String email;

    @NotEmpty
    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL, fetch=FetchType.EAGER)
    private Set<Education> educations;

    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL, fetch=FetchType.EAGER)
    private Set<Skill> skills;

    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL, fetch=FetchType.EAGER)
    private Set<Experience> experiences;

    @ManyToMany(fetch= FetchType.EAGER)
    @JoinTable(joinColumns =  @JoinColumn(name = "person_id"),
            inverseJoinColumns = @JoinColumn(name="role_id"))
    private Collection<Role> roles;


    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL, fetch=FetchType.EAGER)
    private Set<Job> jobs;

    public Set<Job> getJobs() {
        return jobs;
    }

    public void setJobs(Set<Job> jobs) {
        this.jobs = jobs;
    }


    public Collection<Role> getRoles() {
        return roles;
    }

    public void setRoles(Collection<Role> roles) {
        this.roles = roles;
    }




    //add a course list many to many relationship
    @ManyToMany(mappedBy = "students")
    private Set<Course> courselist;

    private boolean courseReg;

    public boolean isCourseReg() {
        return courseReg;
    }

    public void setCourseReg(boolean courseReg) {
        this.courseReg = courseReg;
    }



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
        this.courseReg=false;
        this.roles=new HashSet<Role>();
    }

    //add a new role
    public void addRole(Role role)
    {
        this.roles.add(role);
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
