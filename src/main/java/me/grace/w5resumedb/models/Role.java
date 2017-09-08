package me.grace.w5resumedb.models;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Entity
public class Role {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long roleId;

    @Column(unique=true)
    private String roleName;

    @ManyToMany(mappedBy = "roles", fetch = FetchType.LAZY)
    private Collection<Person> persons;

    //Constructor for Role Class
    public Role()
    {
        this.persons = new ArrayList<Person>();
    }

    //add one user to the user collection
    public void addPerson(Person person)
    {
        this.persons.add(person);
    }

    public long getRoleId() {
        return roleId;
    }

    public void setRoleId(long roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Collection<Person> getPersons() {
        return persons;
    }

    public void setPersons(Collection<Person> persons) {
        this.persons = persons;
    }
}
