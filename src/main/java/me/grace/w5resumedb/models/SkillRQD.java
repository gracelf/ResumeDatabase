package me.grace.w5resumedb.models;


import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.*;

@Entity
public class SkillRQD {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long rSkillId;

    @NotEmpty
    private String rSkillName;

    private String skillDescription;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="job_id")
    private Job job;

    public Job getJob() {
        return job;
    }

    public void setJob(Job job) {
        this.job = job;
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
