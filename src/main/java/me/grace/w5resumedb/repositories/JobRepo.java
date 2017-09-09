package me.grace.w5resumedb.repositories;

import me.grace.w5resumedb.models.Job;
import org.springframework.data.repository.CrudRepository;

public interface JobRepo extends CrudRepository<Job, Long>{

    Iterable<Job> findAllBySkills_skillname(String skillname);

    Iterable<Job> findAllByEmployer(String partialString);

}
