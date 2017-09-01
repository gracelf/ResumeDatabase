package me.grace.w5resumedb;


import me.grace.w5resumedb.models.*;
import me.grace.w5resumedb.repositories.*;
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

    @Autowired
    CourseRepo courseRepo;

    @Autowired
    public SomeIdtoUse personIdReuse;

    @Autowired
    public SomeIdtoUse test;

    //login page


    @GetMapping("/addcourse")
    public String addcourse()
    {
        return "courseform";
    }

    @PostMapping("/addcourse")
    public String postCourse()
    {
        return "courseconfirm";
    }


    @GetMapping("/loadcourse")
    public @ResponseBody String loadcourse()
    {

        Course course1 = new Course();
        course1.setCourseName("Java001");
        courseRepo.save(course1);

        Course course2 = new Course();
        course1.setCourseName("Petrology001");
        courseRepo.save(course1);

        Course course3 = new Course();
        course1.setCourseName("Mineralogy001");
        courseRepo.save(course1);

        Course course4 = new Course();
        course1.setCourseName("Python001");
        courseRepo.save(course1);


        return "courseloaded";
    }

    @GetMapping("/listcourse")
    public String listcourse(Model model)
    {
        model.addAttribute(courseRepo.findAll());
        return "displaycourse";
    }



    @GetMapping("/login")
    public String login(){
        return "login";
    }

    //Home page, request the user to enter their name and email first
    //if user has entered the name, welcome he/she back and check history
    @GetMapping("/")
    public String addPersonandHomepage(Model model)
    {

        Person person = new Person();
        System.out.println(person.getUuid());
        model.addAttribute("newperson", person);
        return "addperson";
    }

    //take person result, display result and allow user to add edu/skill/exp to this person
    @PostMapping("/displayperson")
    public String postperson(@Valid @ModelAttribute("newperson") Person person, BindingResult bindingResult)
    {
        if(bindingResult.hasErrors())
        {
            return "addperson";
        }

        System.out.println("when postMapping form, will the auto generated ID show up:    " +person.getUuid());

        personRepo.save(person);


        //yes the ID show up here!!!!!!!!!!!!!
        System.out.println("After save the person to database, will the auto generated ID show up:    " +person.getUuid());

        return "displayperson";
    }

    //prompt the user to enter education information
    //check the entries existed in the database, if greater than 10, not more input
//    @GetMapping("/addeducation")
//    public String addeducationto(Model model)
//    {
////        System.out.println(educationRepo.count());
//
////        if(educationRepo.count()>=10)
////        {
////            return "limit";
////        }
//
//        model.addAttribute("neweducation", new Education());
//        return "addeducation";
//
//    }
//
//    @PostMapping("/addeducation")
//    public String posteducation(@Valid @ModelAttribute("neweducation") Education education, BindingResult bindingResult)
//    {
//        if(bindingResult.hasErrors())
//        {
//            return "addeducation";
//        }
//
//        educationRepo.save(education);
//        return "displayeducation";
//    }


    /// add education method works!!!!!


    @GetMapping("/addeducationtoperson/{id}")
    public String addEduToPerson(@PathVariable("id") long personId, Model model){

        System.out.println("!!!!!" + personId);

//        String filename = uploadResult.get("public_id").toString() + "." + uploadResult.get("format").toString();
//        model.addAttribute("sizedimageurl", cloudc.createUrl(filename,100,150, "fit"));
//        shoppingCart.addThings(filename);
//        model.addAttribute("filesuploaded", shoppingCart.getThings());

//        long num = 3L;
//        String t=  Long.toString(num);
//
//        int numint = 3;
//        String y = Integer.toString(numint);


        // this works.successfully print out the person ID
        personIdReuse.addThings(Long.toString(personId));
        System.out.println("===testing session variable:   " + personIdReuse.getThings());


        //try use this personIdReuse in Html print out
        String tryprintout = personIdReuse.getThings();

        model.addAttribute("tryprintout", tryprintout);


        Person oneperson= personRepo.findOne(personId);
//
//      personIdReuse.addThings(Long.toString(oneperson.getUuid()));


        model.addAttribute("oneperson", oneperson);

        Education education = new Education();
        //try make an connection with education and person, doesn't work, it has to be right beofore save,
        // the same code in posting works when save the education right after!!!
        //education.setPerson(oneperson);
        //System.out.println(education.getPerson().getUuid());
        model.addAttribute("neweducation", education);
        return "addedutopersonform";
    }

    @PostMapping("/addeducationtoperson/{personid}")
    public String postEdutoPerson(@PathVariable("personid") long personId,  @ModelAttribute("neweducation") Education education, Model model){

        System.out.println("==== personID:   " + personId);

        //this works as well, so it can be used in another method as a variable
        System.out.println("===testing session variable:   " + personIdReuse.getThings());

        //personRepo.findOne(personId).addEdu(education); this doesn't create the relationship, maybe because person is being mapped!!
        //try the following
        education.setPerson(personRepo.findOne(personId));

        //try add it to model and see if it can be printed in HTML
        model.addAttribute(personIdReuse.getThings());

        educationRepo.save(education);
        return "displayeducation";
    }

    ///add skill to a person method works
    @GetMapping("/addskilltoperson/{id}")
    public String addSkillToPerson(@PathVariable("id") long personId, Model model){

        System.out.println("!!!!!" + personId);

        Person oneperson= personRepo.findOne(personId);
        model.addAttribute("oneperson", oneperson);

        Skill skill = new Skill();
        model.addAttribute("newskill", skill);

        return "addskilltopersonform";
    }

    @PostMapping("/addskilltoperson/{personid}")
    public String postskilltoPerson(@PathVariable("personid") long personId,  @ModelAttribute("newskill") Skill skill, Model model){

        System.out.println("==== personID:   " + personId);

        //personRepo.findOne(personId).addEdu(education); this doesn't create the relationship, maybe because person is being mapped!!
        //try the following
        skill.setPerson(personRepo.findOne(personId));
        skillRepo.save(skill);
        return "displayskill";
    }


    //add exptoperson get and post mapping methods!!!  addexptoperson/
    @GetMapping("/addexptoperson/{id}")
    public String addExpToPerson(@PathVariable("id") long personId, Model model){

        System.out.println("!!!!!" + personId);

        Person oneperson= personRepo.findOne(personId);
        model.addAttribute("oneperson", oneperson);

        Experience experience = new Experience();
        model.addAttribute("newexperience", experience);

        return "addexptopersonform";
    }

    @PostMapping("/addexptoperson/{personid}")
    public String postExptoPerson(@PathVariable("personid") long personId,  @ModelAttribute("newexperience") Experience experience, Model model){

        System.out.println("==== personID:   " + personId);

        //personRepo.findOne(personId).addEdu(education); this doesn't create the relationship, maybe because person is being mapped!!
        //try the following
        experience.setPerson(personRepo.findOne(personId));
        experienceRepo.save(experience);
        return "displayexperience";
    }

    @GetMapping("/displaypersonwithid/{id}")
    public String postpersonwithId(@PathVariable("id") long personId, Model model)
    {

        model.addAttribute("newperson", personRepo.findOne(personId));
        return "displayperson";
    }

    @GetMapping("/displayoneprofile/{id}")
    public String displayOneProfile(@PathVariable("id") long personId, Model model)
    {

        System.out.println("=====display one person profile,  person ID:   " + personId);

        model.addAttribute("newperson", personRepo.findOne(personId));

        return "displayoneprofile";
    }

//
//    //promt the user to enter skill information
//    //check the entries existed in the database, if greater than 20, not more input
//    @GetMapping("/addskill")
//    public String addskill(Model model)
//    {
//
////        if(skillRepo.count()>=20)
////        {
////            return "limit";
////        }
//
//        model.addAttribute("newskill", new Skill());
//        return "addskill";
//    }
//
//    @PostMapping("/addskill")
//    public String postskill(@Valid @ModelAttribute("newskill") Skill skill, BindingResult bindingResult)
//    {
//        if(bindingResult.hasErrors())
//        {
//            return "addskill";
//        }
//
//        skillRepo.save(skill);
//        return "displayskill";
//    }
//
//    //promt the user to enter experience information
//    //check the entries existed in the database, if greater than 10, not more input
//    @GetMapping("/addexperience")
//    public String addexperience(Model model)
//    {
//
////        if(experienceRepo.count()>=10)
////        {
////            return "limit";
////        }
//
//        model.addAttribute("newexperience", new Experience());
//        return "addexperience";
//    }
//
//    @PostMapping("/addexperience")
//    public String postexperience(@Valid @ModelAttribute("newexperience") Experience experience, BindingResult bindingResult)
//    {
//        if(bindingResult.hasErrors())
//        {
//            return "addexperience";
//        }
//
//        experienceRepo.save(experience);
//        return "displayexperience";
//    }

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


//        Person person= new Person();
//        Iterable<Person> allperson= personRepo.findAll();
//        model.addAttribute("person",allperson);

//        Iterable<Person> allperson= personRepo.findAll();

        model.addAttribute("allperson", personRepo.findAll());

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


    // this two methods work, even without change the connection between edu/person/, skill/person, exp/person
    // the connection stays the same in database
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


    //update and remove education, both works!!!
    @RequestMapping("/updateedu/{id}")
    public String updateedu(@PathVariable("id") long educationId, Model model){

        Education updateedu = educationRepo.findOne(educationId);

        //String pId= Long.toString(updateedu.getPerson().getUuid());

        Person oneperson = updateedu.getPerson();

        model.addAttribute("neweducation", updateedu );

        model.addAttribute("oneperson", oneperson);

//      return "/addeducationtoperson/" + pId;
        return "addedutopersonform";
    }


    @RequestMapping("/deleteedu/{id}")
    public String deledu(@PathVariable("id") long educationId, Model model){

        Education deleteedu = educationRepo.findOne(educationId);
        Person oneperson = deleteedu.getPerson();

        oneperson.removeEdu(deleteedu);

        educationRepo.delete(educationId);
        return "redirect:/displayall";
    }



    // update and remove skills!!!!!
    @RequestMapping("/updatesk/{id}")
    public String updatesk(@PathVariable("id") long skillId, Model model){

        Skill updateskill = skillRepo.findOne(skillId);

        Person oneperson = updateskill.getPerson();

        model.addAttribute("oneperson", oneperson);

        model.addAttribute("newskill", updateskill);
        return "addskilltopersonform";
    }

    // remove skills, same as edu, remove from person first!!!!!
    @RequestMapping("/deletesk/{id}")
    public String delsk(@PathVariable("id") long skillId){


        Skill deleteskil = skillRepo.findOne(skillId);
        Person oneperson = deleteskil.getPerson();

        oneperson.removeSkl(deleteskil);

        skillRepo.delete(skillId);
        return "redirect:/displayall";
    }

    @RequestMapping("/updateexp/{id}")
    public String updateexp(@PathVariable("id") long experienceId, Model model){

        Experience updateexp = experienceRepo.findOne(experienceId);

        Person oneperson = updateexp.getPerson();

        model.addAttribute("oneperson", oneperson);

        model.addAttribute("newexperience", updateexp);

        return "addexptopersonform";
    }

    @RequestMapping("/deleteexp/{id}")
    public String delexp(@PathVariable("id") long experienceId){

        Experience deleteexp = experienceRepo.findOne(experienceId);
        Person oneperson = deleteexp.getPerson();

        oneperson.removeExp(deleteexp);
        experienceRepo.delete(experienceId);
        return "redirect:/displayall";

    }

}
