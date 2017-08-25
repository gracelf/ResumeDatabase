package me.grace.w5resumedb.repositories;

import me.grace.w5resumedb.models.Skill;
import org.springframework.data.repository.CrudRepository;

public interface SkillRepo extends CrudRepository<Skill,Long> {
}