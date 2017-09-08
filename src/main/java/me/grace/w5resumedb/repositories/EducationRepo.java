package me.grace.w5resumedb.repositories;

import me.grace.w5resumedb.models.Education;
import org.springframework.data.repository.CrudRepository;

import java.util.ArrayList;

public interface EducationRepo extends CrudRepository<Education,Long> {

    ArrayList<Education> findByUniversity(String string);

}
