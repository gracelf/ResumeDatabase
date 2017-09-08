package me.grace.w5resumedb.repositories;

import me.grace.w5resumedb.models.Role;
import org.springframework.data.repository.CrudRepository;

public interface RoleRepo extends CrudRepository<Role, Long> {

    Role findByRoleName(String roleName);
}
