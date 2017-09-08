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
    private String jobDescriprtion;

    //testing different string ArrayList and new skill class
//    @NotEmpty
//    private ArrayList<String> skillList;
//
//    @NotEmpty
//    private String[] skillSlist;

    @ManyToMany(fetch=FetchType.EAGER)
    private Set<SkillRQD> rSkills;

    //constructor for jobs
    public Job(){
        this.rSkills=new HashSet<SkillRQD>();
    }

    //add a new required skill to the job
    public void addskilltojob(SkillRQD s)
    {
        this.getrSkills().add(s);
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

    public String getJobDescriprtion() {
        return jobDescriprtion;
    }

    public void setJobDescriprtion(String jobDescriprtion) {
        this.jobDescriprtion = jobDescriprtion;
    }

    public Set<SkillRQD> getrSkills() {
        return rSkills;
    }

    public void setrSkills(Set<SkillRQD> rSkills) {
        this.rSkills = rSkills;
    }
}
