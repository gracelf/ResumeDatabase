package me.grace.w5resumedb.models;


import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Skill {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long skillId;

    @NotEmpty
    private String skillname;

    @NotEmpty
    private String skillrating;

    @ManyToMany(fetch = FetchType.EAGER)
    private Set<Person> person;

    @ManyToMany(mappedBy = "skills", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Job> jobs;

    //constrcutor for skill
    public Skill(){

        this.person = new HashSet<Person>();
        this.jobs = new HashSet<Job>();

    }

    //add person method, since person is being mapped, skills is the owner side of the person-skill manytomany relationship
    public void addperson(Person p)
    {
        this.person.add(p);
    }

    public Set<Job> getJob() {
        return jobs;
    }

    public void setJob(Set<Job> job) {
        this.jobs = job;
    }

    public Set<Person> getPerson() {
        return person;
    }

    public void setPerson(Set<Person> person) {
        this.person = person;
    }

    public Set<Job> getJobs() {
        return jobs;
    }

    public void setJobs(Set<Job> jobs) {
        this.jobs = jobs;
    }

    public long getSkillId() {
        return skillId;
    }

    public void setSkillId(long skillId) {
        this.skillId = skillId;
    }

    public String getSkillname() {
        return skillname;
    }

    public void setSkillname(String skillname) {
        this.skillname = skillname;
    }

    public String getSkillrating() {
        return skillrating;
    }

    public void setSkillrating(String skillrating) {
        this.skillrating = skillrating;
    }

    public void removePerson (Person p){
        this.person.remove(p);

    }
}
