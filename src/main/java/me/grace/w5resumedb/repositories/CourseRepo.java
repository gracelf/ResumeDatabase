package me.grace.w5resumedb.repositories;

import me.grace.w5resumedb.models.Course;
import org.springframework.data.repository.CrudRepository;

public interface CourseRepo extends CrudRepository<Course,Long>{
}
