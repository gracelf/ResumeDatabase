package me.grace.w5resumedb.models;


import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;
import java.util.Set;

@Entity
public class SkillRQD {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long rSkillId;

    @NotEmpty
    private String rSkillName;

    private String skillDescription;

    @ManyToMany(mappedBy = "rSkills", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Job> jobs;

    public Set<Job> getJob() {
        return jobs;
    }

    public void setJob(Set<Job> job) {
        this.jobs = job;
    }

    public long getrSkillId() {
        return rSkillId;
    }

    public void setrSkillId(long rSkillId) {
        this.rSkillId = rSkillId;
    }

    public String getrSkillName() {
        return rSkillName;
    }

    public void setrSkillName(String rSkillName) {
        this.rSkillName = rSkillName;
    }

    public String getSkillDescription() {
        return skillDescription;
    }

    public void setSkillDescription(String skillDescription) {
        this.skillDescription = skillDescription;
    }
}
