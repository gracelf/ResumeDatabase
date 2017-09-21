package me.grace.w5resumedb;

import me.grace.w5resumedb.models.Person;
import me.grace.w5resumedb.models.Role;
import me.grace.w5resumedb.repositories.PersonRepo;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.Set;

@Transactional
@Service
public class SSUserDetailsService implements UserDetailsService {

    private PersonRepo personRepo;

    public SSUserDetailsService(PersonRepo personRepo)
    {
        this.personRepo=personRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        try{
            Person user= personRepo.findByUsername(username);
            if(user==null){
                System.out.println("user not found this the provide username" + user.toString());
                return null;
            }

            //define principal methods
            System.out.println("user from username " + user.toString());
            return new org.springframework.security.core.userdetails.User(
                    user.getUsername(), user.getPassword(),getAuthorities(user));
        } catch (Exception e){
            throw new UsernameNotFoundException("User not found");
        }
    }

    private Set<GrantedAuthority> getAuthorities(Person user){
        Set<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();
        for(Role role: user.getRoles()){
            GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(role.getRoleName());
            authorities.add(grantedAuthority);
        }

        System.out.println("user authorities are " +authorities.toString());
        return authorities;
    }

}
