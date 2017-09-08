package me.grace.w5resumedb.repositories;

import me.grace.w5resumedb.models.Person;
import org.springframework.data.repository.CrudRepository;

public interface PersonRepo extends CrudRepository<Person,Long> {

    Person findByUsername(String username);
    Person findByEmail(String email);
    Long countByEmail(String email);
    Long countByUsername(String username);

    Iterable<Person> findAllByFirstName(String firstName);

    Iterable<Person> findAllByFirstNameandAndLastName(String firstName, String lastName);

    Iterable<Person> findAllByUniversity(String firstName);

    Iterable<Person> findAllByExperiences_organization(String organizationName);


}
