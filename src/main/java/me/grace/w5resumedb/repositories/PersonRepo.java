package me.grace.w5resumedb.repositories;

import me.grace.w5resumedb.models.Person;
import org.springframework.data.repository.CrudRepository;

public interface PersonRepo extends CrudRepository<Person,Long> {
}
