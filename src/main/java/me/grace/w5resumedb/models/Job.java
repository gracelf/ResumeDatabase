package me.grace.w5resumedb.models;


import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long jobId;

    @NotEmpty
    private String title;

    @NotEmpty
    private String employer;

    @NotEmpty
    private String salaryRange;

    @NotEmpty
    private String jobDescription;

    //testing different string ArrayList and new skill class
//    @NotEmpty
//    private ArrayList<String> skillList;
//
//    @NotEmpty
//    private String[] skillSlist;

    @ManyToMany(fetch=FetchType.EAGER)
    private Set<Skill> skills;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="recruiter_id")
    private Person person;

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    //constructor for jobs
    public Job(){
        this.skills=new HashSet<Skill>();
    }

    //add a new required skill to the job
    public void addskilltojob(Skill s)
    {
        this.skills.add(s);
    }

    public long getJobId() {
        return jobId;
    }

    public void setJobId(long jobId) {
        this.jobId = jobId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEmployer() {
        return employer;
    }

    public void setEmployer(String employer) {
        this.employer = employer;
    }

    public String getSalaryRange() {
        return salaryRange;
    }

    public void setSalaryRange(String salaryRange) {
        this.salaryRange = salaryRange;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public void setJobDescription(String jobDescription) {
        this.jobDescription = jobDescription;
    }

    public Set<Skill> getSkills() {
        return skills;
    }

    public void setSkills(Set<Skill> skills) {
        this.skills = skills;
    }
}
