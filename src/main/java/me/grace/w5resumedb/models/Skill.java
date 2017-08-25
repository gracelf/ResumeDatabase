package me.grace.w5resumedb.models;


import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
public class Skill {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long skillId;

    @NotNull
    @Size(min=3)
    private String skillname;

    @NotNull
    @Size(min=3)
    private String skillrating;


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
}
