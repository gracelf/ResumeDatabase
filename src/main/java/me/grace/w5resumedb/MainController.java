package me.grace.w5resumedb;


import me.grace.w5resumedb.models.Education;
import me.grace.w5resumedb.models.Experience;
import me.grace.w5resumedb.models.Person;
import me.grace.w5resumedb.models.Skill;
import me.grace.w5resumedb.repositories.EducationRepo;
import me.grace.w5resumedb.repositories.ExperienceRepo;
import me.grace.w5resumedb.repositories.PersonRepo;
import me.grace.w5resumedb.repositories.SkillRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;

@Controller
public class MainController {

    @Autowired
    PersonRepo personRepo;
    @Autowired
    EducationRepo educationRepo;
    @Autowired
    SkillRepo skillRepo;
    @Autowired
    ExperienceRepo experienceRepo;

    //Home page, request the user to enter their name and email first
    //if user has entered the name, welcome he/she back and check history
    @GetMapping("/")
    public String addPersonandHomepage(Model model)
    {

        model.addAttribute("newperson", new Person());
        return "addperson";
    }

    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @PostMapping("/")
    public String postperson(@Valid @ModelAttribute("newperson") Person person, BindingResult bindingResult)
    {
        if(bindingResult.hasErrors())
        {
            return "addperson";
        }

        personRepo.save(person);
        return "displayperson";
    }

    //prompt the user to enter education information
    //check the entries existed in the database, if greater than 10, not more input
    @GetMapping("/addeducation")
    public String addeducationto(Model model)
    {
        System.out.println(educationRepo.count());

//        if(educationRepo.count()>=10)
//        {
//            return "limit";
//        }

        model.addAttribute("neweducation", new Education());
        return "addeducation";

    }

    @PostMapping("/addeducation")
    public String posteducation(@Valid @ModelAttribute("neweducation") Education education, BindingResult bindingResult)
    {
        if(bindingResult.hasErrors())
        {
            return "addeducation";
        }

        educationRepo.save(education);
        return "displayeducation";
    }

    @GetMapping("/addeducationtoperson/{id}")
    public String addEduToPerson(){

        return "addedutopersonform";
    }



    //promt the user to enter skill information
    //check the entries existed in the database, if greater than 20, not more input
    @GetMapping("/addskill")
    public String addskill(Model model)
    {

//        if(skillRepo.count()>=20)
//        {
//            return "limit";
//        }

        model.addAttribute("newskill", new Skill());
        return "addskill";
    }

    @PostMapping("/addskill")
    public String postskill(@Valid @ModelAttribute("newskill") Skill skill, BindingResult bindingResult)
    {
        if(bindingResult.hasErrors())
        {
            return "addskill";
        }

        skillRepo.save(skill);
        return "displayskill";
    }

    //promt the user to enter experience information
    //check the entries existed in the database, if greater than 10, not more input
    @GetMapping("/addexperience")
    public String addexperience(Model model)
    {

//        if(experienceRepo.count()>=10)
//        {
//            return "limit";
//        }

        model.addAttribute("newexperience", new Experience());
        return "addexperience";
    }

    @PostMapping("/addexperience")
    public String postexperience(@Valid @ModelAttribute("newexperience") Experience experience, BindingResult bindingResult)
    {
        if(bindingResult.hasErrors())
        {
            return "addexperience";
        }

        experienceRepo.save(experience);
        return "displayexperience";
    }

    //if person, skill or education is blank, will ask the user to input
    @GetMapping("/displayall")
    public String getall(Model model)
    {
//        if(personRepo.count()==0)
//        {
//            return "moreinfo";
//        }
//
//        if(educationRepo.count()==0)
//        {
//            return "moreinfo";
//        }
//
//        if(skillRepo.count()==0)
//        {
//            return "moreinfo";
//        }


        Person person= new Person();
        Iterable<Person> allperson= personRepo.findAll();
        model.addAttribute("person",allperson);

//        //Get information from database, pass on to the person object
//        Iterable<Education> alledu= educationRepo.findAll();
//        ArrayList<Education> educa= new ArrayList<>();
//        educa= (ArrayList<Education>) alledu;
//        person.setEducations(educa);
//        model.addAttribute("alledu",person.getEducations());
//
//        Iterable<Skill> allskill= skillRepo.findAll();
//        ArrayList<Skill> skills= new ArrayList<>();
//        skills= (ArrayList<Skill>) allskill;
//        person.setSkills(skills);
//        model.addAttribute("allskill",person.getSkills());
//
//        Iterable<Experience> allexp= experienceRepo.findAll();
//        ArrayList<Experience> exps= new ArrayList<>();
//        exps= (ArrayList<Experience>) allexp;
//        person.setExperiences(exps);
//        model.addAttribute("allexp",person.getExperiences());

        return "displayall";

    }


    @RequestMapping("/updateper/{id}")
    public String updateper(@PathVariable("id") long uuid, Model model){
        model.addAttribute("newperson", personRepo.findOne(uuid));
        return "addperson";
    }

    @RequestMapping("/deleteper/{id}")
    public String delper(@PathVariable("id") long uuid){
        personRepo.delete(uuid);
        return "redirect:/displayall";
    }


    @RequestMapping("/updateedu/{id}")
    public String updateedu(@PathVariable("id") long educationId, Model model){
        model.addAttribute("neweducation", educationRepo.findOne(educationId));
        return "addeducation";
    }

    @RequestMapping("/deleteedu/{id}")
    public String deledu(@PathVariable("id") long id){
        educationRepo.delete(id);
        return "redirect:/displayall";
    }


    @RequestMapping("/updatesk/{id}")
    public String updatesk(@PathVariable("id") long skillId, Model model){
        model.addAttribute("newskill", skillRepo.findOne(skillId));
        return "addskill";
    }

    @RequestMapping("/deletesk/{id}")
    public String delsk(@PathVariable("id") long skillId){
        skillRepo.delete(skillId);
        return "redirect:/displayall";
    }

    @RequestMapping("/updateexp/{id}")
    public String updateexp(@PathVariable("id") long experienceId, Model model){
        model.addAttribute("newexperience", experienceRepo.findOne(experienceId));
        return "addexperience";
    }

    @RequestMapping("/deleteexp/{id}")
    public String delexp(@PathVariable("id") long experienceId){
        experienceRepo.delete(experienceId);
        return "redirect:/displayall";
    }

}
