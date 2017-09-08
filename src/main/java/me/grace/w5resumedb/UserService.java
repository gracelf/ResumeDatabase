package me.grace.w5resumedb;

import me.grace.w5resumedb.models.Person;
import me.grace.w5resumedb.repositories.PersonRepo;
import me.grace.w5resumedb.repositories.RoleRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;



    @Service
    public class UserService {

        @Autowired
        PersonRepo personRepo;

        @Autowired
        RoleRepo roleRepo;

        @Autowired
        public UserService(PersonRepo personRepo) {
            this.personRepo = personRepo;
        }

        public Person findbyEmail(String email) {
            return personRepo.findByEmail(email);
        }

        public Long countByEmail(String email) {
            return personRepo.countByEmail(email);
        }

        public Person findByUsername(String username) {
            return personRepo.findByUsername(username);
        }

        public void saveJobSeeker(Person user) {
            user.setRoles(Arrays.asList(roleRepo.findByRoleName("JOBSEEKER")));
            user.setEnabled(true);
            personRepo.save(user);
        }

        public void saveRecruiter(Person user) {
            user.setRoles(Arrays.asList(roleRepo.findByRoleName("RECRUITER")));
            user.setEnabled(true);
            personRepo.save(user);
        }
    }


