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
    public SomeIdtoUse courseId;

    //login page


    @GetMapping("/addcourse")
    public String addcourse(Model model)
    {
        Course course = new Course();
        model.addAttribute("newCourse", course);
        return "courseform";
    }

    @PostMapping("/addcourse")
    public String postCourse(@Valid @ModelAttribute("newCourse") Course course, BindingResult bindingResult, Model model)
    {

        if(bindingResult.hasErrors())
        {
            return "courseform";
        }

        courseRepo.save(course);

        System.out.println("when postMapping form, will the auto generated ID show up:    " +course.getCourseId());

        courseId.setCourseId(course.getCourseId());

        System.out.println("==========component testing ========" + courseId.getCourseId());

        return "courseconfirm";
    }


    @GetMapping("/loadcourse")
    public @ResponseBody String loadcourse()
    {

        Course course1 = new Course();
        course1.setCourseCode("Java001");
        course1.setCredit(3.0);
        courseRepo.save(course1);

        Course course2 = new Course();
        course2.setCourseCode("Statistics001");
        course2.setCredit(3.5);
        courseRepo.save(course2);

        Course course3 = new Course();
        course3.setCourseCode("Mineralogy001");
        course3.setCredit(3.0);
        courseRepo.save(course3);

        Course course4 = new Course();
        course4.setCourseCode("Python001");
        course4.setCredit(2.5);
        courseRepo.save(course4);

//        Person personN= new Person();
//        personN.setFirstName("Grace");
//        personN.setCourseReg(true);
//        personN.setUuid(9L);

        return "courseloaded";
    }

    @GetMapping("/addstudentstocourse/{id}")
    public String addstudentstocourse(@PathVariable("id") long courseId, Model model)
    {
        //try if the session variable will work here

        System.out.println("--------"+courseId);
//        System.out.println("add studnets to course Id testing =======" + courseId.getCourseId());
        Course newCourse= courseRepo.findOne(courseId);


        model.addAttribute("newCourse",newCourse);
        System.out.println("here0==========" + newCourse.getCourseId());


        Iterable<Person> allstudents = personRepo.findAll();

        ArrayList<Person> allstudent = (ArrayList<Person>) allstudents;


        System.out.println("here==========");

        model.addAttribute("allstudents",allstudent );

        System.out.println(allstudents.iterator().next().getUuid());
        System.out.println(allstudents.iterator().next().getFirstName());
        System.out.println(allstudents.iterator().next().getLastName());
        System.out.println(allstudents.iterator().next().isCourseReg());

        System.out.println("here2==========");

//      model.addAllAttributes("checkedstudentsId", long [] checkedstudentsId);

        return "addstudenttocourseform";

//        long course1Id = 0L;
//        long course2Id = 0L;
//        long course3Id = 0L;
//        long course4Id = 0L;
//        long course5Id= 0L;
//
//        newCourse.getStudents().iterator().next().setUuid(course1Id);
//        newCourse.getStudents().iterator().next().setUuid(course2Id);
//        newCourse.getStudents().iterator().next().setUuid(course3Id);
//        newCourse.getStudents().iterator().next().setUuid(course4Id);
//        newCourse.getStudents().iterator().next().setUuid(course5Id);
//
//        newCourse.setStudents(personRepo.findAll());
//
//        System.out.println("////////" + newCourse.getStudents().iterator().next().getUuid());
//
//        model.addAttribute("newCourse", newCourse);
//
//        courseRepo.findOne(courseId.getCourseId()).getStudents().iterator().next().getUuid();

        //return courseRepo.findOne(courseId.getCourseId()).getStudents().iterator().hasNext();

    }

    @PostMapping("/addstudentstocourse/{id}")
    public String poststudenttocourse(@PathVariable("id") long courseId, @RequestParam(value="studentsIds") Long[] checkedstudentsId, @ModelAttribute ("allstudents") ArrayList<Person> allstudent,  Model model)
    {

        Course newCourse= courseRepo.findOne(courseId);

        System.out.println("Step1=========");

        System.out.println(checkedstudentsId[0]);

        for(long studentId: checkedstudentsId)
        {
            System.out.println("+++++++++" + studentId);
            newCourse.addPersontoCourse(personRepo.findOne(studentId));

        }
        courseRepo.save(newCourse);

//        System.out.println(allstudents.iterator().next().getLastName());
//
//        System.out.println(allstudents.iterator().next().isCourseReg());
//
//        for (Person p:allstudents)
//        {
//
//            System.out.println(p.isCourseReg());
////            if (p.isCourseReg()==true){
////                newCourse.addPersontoCourse(p);
////            }
//
//        }
//
//        courseRepo.save(newCourse);
//
//        System.out.println("testing======="  +newCourse.getStudents().iterator().next().getUuid());

        return "onecourselist";
    }



    @GetMapping("/listallcourses")
    public String listcourse(Model model)
    {
        model.addAttribute("allcourses", courseRepo.findAll());
        return "listallcourses";
    }


    @GetMapping("/login")
    public String login(){
        return "login";
    }

    //Home page, request the user to enter their name and email first
    //if user has entered the name, welcome he/she back and check history


    @GetMapping("/")
    public String homepage(){

        return "homepage";
    }


    @GetMapping("/addperson")
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

//    @GetMapping("/displaypersonwithid/{id}")
//    public String postpersonwithId(@PathVariable("id") long personId, Model model)
//    {
//
//        model.addAttribute("newperson", personRepo.findOne(personId));
//        return "displayperson";
//    }

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
        personIdReuse.setPersonId(personId);
        System.out.println("===testing session variable:   " + personIdReuse.getPersonId());


        //try use this personIdReuse in Html print out

        model.addAttribute("tryprintout", personIdReuse.getPersonId());


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
    public String postEdutoPerson(@PathVariable("personid") long personId,  @ModelAttribute("neweducation") Education education, BindingResult bindingResult, Model model){


        if(bindingResult.hasErrors())
        {
            return "/addeducationtoperson/{id}";
        }

        System.out.println("==== personID:   " + personId);

        //this works as well, so it can be used in another method as a variable
        System.out.println("===testing session variable:   " + personIdReuse.getPersonId());

        //personRepo.findOne(personId).addEdu(education); this doesn't create the relationship, maybe because person is being mapped!!
        //try the following
        education.setPerson(personRepo.findOne(personId));

        //try add it to model and see if it can be printed in HTML
        model.addAttribute(personIdReuse.getPersonId());

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
    public String postskilltoPerson(@PathVariable("personid") long personId,  @ModelAttribute("newskill") Skill skill, BindingResult bindingResult, Model model){

        if(bindingResult.hasErrors())
        {
            return "/addskilltoperson/{personid}";
        }

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
    public String postExptoPerson(@PathVariable("personid") long personId,  @ModelAttribute("newexperience") Experience experience, BindingResult bindingResult, Model model){

        if(bindingResult.hasErrors())
        {
            return "/addexptoperson/{personid}";
        }

        System.out.println("==== personID:   " + personId);

        //personRepo.findOne(personId).addEdu(education); this doesn't create the relationship, maybe because person is being mapped!!
        //try the following
        experience.setPerson(personRepo.findOne(personId));
        experienceRepo.save(experience);
        return "displayexperience";
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

//    //if person, skill or education is blank, will ask the user to input
    // display the whole list with details, not need for this project,, just for testing
//    @GetMapping("/displayall")
//    public String getall(Model model)
//    {
////        if(personRepo.count()==0)
////        {
////            return "moreinfo";
////        }
////
////        if(educationRepo.count()==0)
////        {
////            return "moreinfo";
////        }
////
////        if(skillRepo.count()==0)
////        {
////            return "moreinfo";
////        }
//
//
////        Person person= new Person();
////        Iterable<Person> allperson= personRepo.findAll();
////        model.addAttribute("person",allperson);
//
////        Iterable<Person> allperson= personRepo.findAll();
//
//        model.addAttribute("allperson", personRepo.findAll());
//
////        //Get information from database, pass on to the person object
////        Iterable<Education> alledu= educationRepo.findAll();
////        ArrayList<Education> educa= new ArrayList<>();
////        educa= (ArrayList<Education>) alledu;
////        person.setEducations(educa);
////        model.addAttribute("alledu",person.getEducations());
////
////        Iterable<Skill> allskill= skillRepo.findAll();
////        ArrayList<Skill> skills= new ArrayList<>();
////        skills= (ArrayList<Skill>) allskill;
////        person.setSkills(skills);
////        model.addAttribute("allskill",person.getSkills());
////
////        Iterable<Experience> allexp= experienceRepo.findAll();
////        ArrayList<Experience> exps= new ArrayList<>();
////        exps= (ArrayList<Experience>) allexp;
////        person.setExperiences(exps);
////        model.addAttribute("allexp",person.getExperiences());
//
//        return "displayall";
//
//    }

    @GetMapping("/listnames")
    public String listNames(Model model){

        model.addAttribute("allperson", personRepo.findAll());
        return "listnames";
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
        return "redirect:/listnames";
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
        long personToGoTo = oneperson.getUuid();
        oneperson.removeEdu(deleteedu);
        educationRepo.delete(educationId);
        return "redirect:/displayoneprofile/" + personToGoTo;
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
        long personToGoTo = oneperson.getUuid();

        oneperson.removeSkl(deleteskil);

        skillRepo.delete(skillId);
        return "redirect:/displayoneprofile/" + personToGoTo;
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
        long personToGoTo = oneperson.getUuid();
        oneperson.removeExp(deleteexp);
        experienceRepo.delete(experienceId);

        return "redirect:/displayoneprofile/" + personToGoTo;

    }

}
