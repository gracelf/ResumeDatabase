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
import java.util.List;
import java.security.Principal;


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
    JobRepo jobRepo;
    @Autowired
    RoleRepo roleRepo;

    @Autowired
    private UserService userService;

    @Autowired
    CourseRepo courseRepo;

    @Autowired
    public SomeIdtoUse personIdReuse;

    @Autowired
    public SomeIdtoUse courseId;


    @GetMapping("/loadroles")
    public @ResponseBody
    String loadroles() {
        Role newrole = new Role();
        newrole.setRoleName("JOBSEEKER");
        roleRepo.save(newrole);
        Role newrole2 = new Role();
        newrole2.setRoleName("RECRUITER");
        roleRepo.save(newrole2);

        return "roles loaded";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    //Home page, request the user to enter their name and email first
    //if user has entered the name, welcome he/she back and check history
    // TODO: add href for 1:login option or register for (jobseeker/recruiter roles)


    // on the homepage, when the person's role is A job Seeker, check if there is a job matching the skills this person have
    @GetMapping("/")
    public String homepage(Principal p, Model model) {

        //in oder to use old route, will fix later
        model.addAttribute("newperson", personRepo.findByUsername(p.getName()));

         // if the person has a role of jobseek, check if there are jobs matches the skill, and send out notification!!!!
        if (personRepo.findByUsername(p.getName()).getRoles().iterator().next().getRoleName().equalsIgnoreCase("JOBSEEKER")) {

        System.out.println("------" + p.getName());

        System.out.println("---------role:" + personRepo.findByUsername(p.getName()).getRoles().iterator().next().getRoleName());

        Person person = userService.findByUsername(p.getName());

        ArrayList<Job> matchingJob =  new ArrayList<Job>();

        if (person.getSkills().isEmpty())
        {
            model.addAttribute("message", "Welcome to our Resume database. Please enter your skills to your resume, and we will check for the matching job for you!");
            model.addAttribute("matchingJob", matchingJob);
        }

        else{

            for (Skill s : person.getSkills()) {

                if (jobRepo.findAllBySkills_skillname(s.getSkillname()) != null) {
                    ArrayList<Job> alljobs = (ArrayList<Job>) jobRepo.findAllBySkills_skillname(s.getSkillname());

                    for(Job item:alljobs)
                    {
                        matchingJob.add(item);
                    }

                }
            }
            if (matchingJob.isEmpty())
            {
                model.addAttribute("message", "No job matches your the skill you have, come back later!");
                model.addAttribute("matchingJob", matchingJob);
            }
            else{
                model.addAttribute("message", "We find some job postings that match your skill");
                model.addAttribute("matchingJob", matchingJob);

            }
        }

            return "notificationS";
        }

        //for recruiter, will have a notification method like the above later!
        else {
            return "homepage";
        }

        //return "homepage";
    }

    @GetMapping("displaypersonalposting")
    public String displaypersonalposting(Principal p, Model model)
    {
        Person pperson = personRepo.findByUsername(p.getName());

        model.addAttribute("jobs", pperson.getJobs());

        return "displaypersonalposting";
    }

    @GetMapping("listallposting")
    public String listallposting( Model model)
    {

        model.addAttribute("alljobs", jobRepo.findAll());
        return "listallposting";
    }

    //testing search method
//    @GetMapping("/findjobbyskill")
//    public @ResponseBody
//    String findjobthroughskill()
//
//    {
//
//        jobRepo.findAllBySkills_skillname("Java");
//        System.out.println(">>>>>>>>" + jobRepo.findAllBySkills_skillname("Java").iterator().next().getJobId());
//
//        Iterable<Person> find = personRepo.findAllByFirstNameAndLastName("Jim", "Berkerly");
//        System.out.println("////" + find.iterator().next().getUuid());
//
//
//        return "find job by skill";
//    }

    //register as a job seeker role
    @RequestMapping(value = "/registerS", method = RequestMethod.GET)
    public String registerasjobseeker(Model model) {
        model.addAttribute("jobSeeker", new Person());
        return "registerSform";
    }

    @RequestMapping(value = "/registerS", method = RequestMethod.POST)
    public String regSpost(@Valid @ModelAttribute("jobSeeker") Person person, BindingResult bResult, Model model) {

        if (bResult.hasErrors()) {
            return "registerSform";
        } else {
            userService.saveJobSeeker(person);
            model.addAttribute("message", "JobSeeker Account Successfully Created");
        }
        return "login";
    }

//    //welcome and notification, and narvbars as well
//    @GetMapping("/homepageS")
//    public String homepageforseeker(Principal p, Model model)
//    {
//
//
//        System.out.println("------" + p.getName());
//
//        Person person = userService.findByUsername(p.getName());
//
//        ArrayList<Job> matchingJob =  new ArrayList<Job>();
//
//        if (person.getSkills().isEmpty())
//        {
//            model.addAttribute("message", "Welcome to our Resume database. Please enter your skills to your resume, and we will check for the matching job for you!");
//        }
//
//        else{
//
//            for (Skill s : person.getSkills()) {
//
//                if (jobRepo.findAllBySkills_skillname(s.getSkillname()) != null) {
//                    ArrayList<Job> alljobs = (ArrayList<Job>) jobRepo.findAllBySkills_skillname(s.getSkillname());
//
//                    for(Job item:alljobs)
//                    {
//                        matchingJob.add(item);
//                    }
//
//                }
//            }
//            if (matchingJob.isEmpty())
//            {
//                model.addAttribute("message", "No job matches your the skill you have, come back later!");
//            }
//            else{
//                model.addAttribute("message", "We find some job postings that match your skill");
//                model.addAttribute("matchingJob", matchingJob);
//
//            }
//        }
//
//        return "notification";
//
//    }

    //register as a job seeker role
    @RequestMapping(value = "/registerR", method = RequestMethod.GET)
    public String registerasR(Model model) {
        model.addAttribute("recruiter", new Person());
        return "registerRform";
    }

    @RequestMapping(value = "/registerR", method = RequestMethod.POST)
    public String regRpost(@Valid @ModelAttribute("recruiter") Person person, BindingResult bResult, Model model) {

        if (bResult.hasErrors()) {
            return "registerRform";
        } else {
            userService.saveRecruiter(person);
            model.addAttribute("message", "JobSeeker Account Successfully Created");
        }
        return "login";
    }


    @GetMapping("/addjob")
    public String addjob(Principal p, Model model) {

        System.out.println("======WHO is the principal/user now? =========" + p.getName());

        // link person and the job they posted

        Job job = new Job();
        model.addAttribute("newJob", job);
        model.addAttribute("allskills", skillRepo.findAll());

        //try add upto 5 skills on the same page
//        Skill otherskill = new Skill();
//        Skill otherskill2 = new Skill();
//        Skill otherskill3 = new Skill();
//        Skill skill1= new Skill();
//        Skill skill2= new Skill();
//        Skill skill3= new Skill();
//        Skill skill4= new Skill();
//        Skill skill5= new Skill();
//        ArrayList<Skill> arr = new ArrayList<Skill>();
//        arr.add(skill1);
//        arr.add(skill2);
//        arr.add(skill3);
//        arr.add(skill4);
//        arr.add(skill5);
//
//        model.addAttribute("arr", arr);
//        model.addAttribute("otherskill", otherskill);
//        model.addAttribute("otherskill2", otherskill2);
//        model.addAttribute("otherskill3", otherskill3);

        return "jobform";

    }


    // if not checked try catch java.lang.NullPointerException: null
    //changed to use @RequestParam to pass skillname and skillrating from the HTML input, this way, null values are allowed.
    // If set as an instance of Skill class, which has constraints on skillname and rating variable, null values are not allowed.
    @PostMapping("/postjob")
    public String postJob(Principal p, @Valid @ModelAttribute("newJob") Job job, @RequestParam(value = "skillIds", required = false) Long[] skillIdchecked,
                          @RequestParam(value = "skillname1", required = false) String skillname1,
                          @RequestParam(value = "skillrating1", required = false) String skillrating1, BindingResult bindingResult, Model model) {

        //System.out.print(arr.iterator().hasNext());

        if(bindingResult.hasErrors())
        {
            return "jobform";
        }

        try {
            for (Long skillId : skillIdchecked) {
                if (skillId != null) {
                    System.out.println("+++++++++" + skillId);
                    job.addskilltojob(skillRepo.findOne(skillId));
                }

            }
        } catch (Exception e) {
            System.out.println("User didn't check any option from db");
        }


//        for (Skill item: arr)
//        {
//            if (item.toString()!=null) {
//                System.out.println("===== if it is empty or not" +item.toString());
//                skillRepo.save(item);
//                job.addskilltojob(item);
//            }
//        }


        try {
            if (skillname1.toString() != null && skillrating1.toString() != null) {

                Skill skill1 = new Skill();
                skill1.setSkillname(skillname1);
                skill1.setSkillrating(skillrating1);

                skillRepo.save(skill1);
                job.addskilltojob(skill1);
            }
        } catch (Exception e) {
            System.out.println("User didn't input other skills");
        }



        //link recruiter and their job postings
        job.setPerson(personRepo.findByUsername(p.getName()));

        jobRepo.save(job);

        model.addAttribute("newJob", job);
        model.addAttribute(("pName"), p.getName());


        return "jobconfirmation";
    }


    @GetMapping("/addskillstojob/{id}")
    public String addskillstojob(@PathVariable("id") long jobId, Model model) {

        Job job = jobRepo.findOne(jobId);
        model.addAttribute("newJob", job);

        System.out.println("====" + jobId);
        model.addAttribute("allskills", skillRepo.findAll());

        return "addskillstojob";
    }

    @PostMapping("/addskillstojob/{id}")
    public String postskilltojob(Principal p, @PathVariable("id") long jobId, @RequestParam(value = "skillIds", required = false) Long[] skillIdchecked,
                                 @RequestParam(value = "skillname1", required = false) String skillname1,
                                 @RequestParam(value = "skillrating1", required = false) String skillrating1, Model model) {

        //System.out.print(arr.iterator().hasNext());

        Job jobtemp = jobRepo.findOne(jobId);

        try {
            for (Long skillId : skillIdchecked) {
                if (skillId != null) {
                    System.out.println("+++++++++" + skillId);
                    jobtemp.addskilltojob(skillRepo.findOne(skillId));
                }

            }
        } catch (Exception e) {
            System.out.println("User didn't check any option from db");
        }


//        for (Skill item: arr)
//        {
//            if (item.toString()!=null) {
//                System.out.println("===== if it is empty or not" +item.toString());
//                skillRepo.save(item);
//                job.addskilltojob(item);
//            }
//        }
        try {
            if (skillname1.toString() != null && skillrating1.toString() != null) {

                Skill skill1 = new Skill();
                skill1.setSkillname(skillname1);
                skill1.setSkillrating(skillrating1);

                skillRepo.save(skill1);
                jobtemp.addskilltojob(skill1);
            }
        } catch (Exception e) {
            System.out.println("User didn't input other skills");
        }

        jobRepo.save(jobtemp);

        model.addAttribute("newJob", jobtemp);
        model.addAttribute(("pName"), p.getName());

        return "jobconfirmation";
    }

    //new add skill to person methods (based on manytomany relationship)
    @GetMapping("/addskilltoperson")
    public String addskillstopersonnew(Principal p, Model model) {


        System.out.println("====" + p.getName());
        model.addAttribute("allskills", skillRepo.findAll());

        return "addskillstopersonformP";
    }

    @PostMapping("/addskillstoperson")
    public String postskilltopernew(Principal p, @RequestParam(value = "skillIds", required = false) Long[] skillIdchecked,
                                 @RequestParam(value = "skillname1", required = false) String skillname1,
                                 @RequestParam(value = "skillrating1", required = false) String skillrating1, Model model) {

        //System.out.print(arr.iterator().hasNext());


        //if choose from exsiting skills, not need to add to repo, just set the person for that skill
        Person pperson = personRepo.findByUsername(p.getName());

        try {
            for (Long skillId : skillIdchecked) {
                if (skillId != null) {
                    System.out.println("+++++++++" + skillId);
                    skillRepo.findOne(skillId).addperson(pperson);
                }

            }
        } catch (Exception e) {
            System.out.println("User didn't check any option from db");
        }


//        for (Skill item: arr)
//        {
//            if (item.toString()!=null) {
//                System.out.println("===== if it is empty or not" +item.toString());
//                skillRepo.save(item);
//                job.addskilltojob(item);
//            }
//        }
        try {
            if (skillname1.toString() != null && skillrating1.toString() != null) {

                Skill skill1 = new Skill();
                skill1.setSkillname(skillname1);
                skill1.setSkillrating(skillrating1);

                skill1.addperson(pperson);
                skillRepo.save(skill1);
            }
        } catch (Exception e) {
            System.out.println("User didn't input other skills");
        }


        model.addAttribute("pperson", pperson);

        return "displayallskillsofp";
    }





    @GetMapping("/displayonejob/{id}")
    public String displayonejob(@PathVariable("id") long jobId, Model model) {
        System.out.println("=====display one job,  Job ID:   " + jobId);

        model.addAttribute("newJob", jobRepo.findOne(jobId));

        return "displayonejob";
    }

    //for testing notification method on student side
    @GetMapping("/notification")
    public String notification(Principal p, Model model) {

        System.out.println("------" + p.getName());

        Person person = userService.findByUsername(p.getName());

        ArrayList<Job> matchingJob =  new ArrayList<Job>();

        if (person.getSkills().isEmpty())
        {
            model.addAttribute("message", "Please enter your skills to your resume!");
            return "message";
        }

        else{

            for (Skill s : person.getSkills()) {

                if (jobRepo.findAllBySkills_skillname(s.getSkillname()) != null) {
                    ArrayList<Job> alljobs = (ArrayList<Job>) jobRepo.findAllBySkills_skillname(s.getSkillname());

                    for(Job item:alljobs)
                    {
                        matchingJob.add(item);
                    }

                }
            }
            if (matchingJob.isEmpty())
            {
                model.addAttribute("message", "No job matches your the skill you entered, come back later!");
                return "message";
            }
            else{
                model.addAttribute("message", "We find some job postings that match your skill");
                model.addAttribute("matchingJob", matchingJob);
                return "notification";
            }
        }


    }


    @GetMapping("/search")
    public String oneSearchlinkforall()
    {

        return "searchform";
    }


    //to accept a string from input in PostMapping, @RequestParam is required
    @PostMapping("/searchbyConame")
    public String searchbyConame(@RequestParam("searchCoName") String searchCoName, Model model){

        System.out.println(searchCoName);

        Iterable<Job> searchResult= jobRepo.findAllByEmployer(searchCoName);
        model.addAttribute("searchResult", searchResult);
        return "jobsearchresult";
    }


    @PostMapping("/searchbySchoolname")
    public String searchbyschoolname(@RequestParam("searchScName") String searchScName, Model model){

        System.out.println(searchScName);

        Iterable<Person> searchResult= personRepo.findAllByEducations_university(searchScName);
        model.addAttribute("searchResult", searchResult);
        return "personsearchresult";
    }

    @PostMapping("/searchbyfirstname")
    public String searchbyfirstname(@RequestParam("searchFirstName") String searchFirstName, Model model){

        System.out.println(searchFirstName);

        Iterable<Person> searchResult= personRepo.findAllByFirstName(searchFirstName);
        model.addAttribute("searchResult", searchResult);
        return "personsearchresult";
    }

    @PostMapping("/searchbyfullname")
    public String searchbyfullname(@RequestParam("searchFName") String searchFName,
                                   @RequestParam("searchLName") String searchLName,Model model){

        System.out.println(searchFName + "  " + searchLName);

        Iterable<Person> searchResult= personRepo.findAllByFirstNameAndLastName(searchFName, searchLName);
        model.addAttribute("searchResult", searchResult);
        return "personsearchresult";
    }


    @GetMapping("/loadskills")
    public @ResponseBody String loadrqdskills()
    {
        Skill skill1 = new Skill();
        skill1.setSkillname("Java");
        skill1.setSkillrating("proficient");
        skillRepo.save(skill1);

        Skill skill2 = new Skill();
        skill2.setSkillname("Python");
        skill2.setSkillrating("Intermediate level");
        skillRepo.save(skill2);

        return "successfully load some skills";
    }



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
        course1.setInstructor("Dr.Eric Ryan");
        course1.setCredit(3.0);
        courseRepo.save(course1);

        Course course2 = new Course();
        course2.setCourseCode("Statistics001");
        course2.setInstructor("Dr.Carol Levy");
        course2.setCredit(3.5);
        courseRepo.save(course2);

        Course course3 = new Course();
        course3.setCourseCode("Mineralogy001");
        course3.setInstructor("Dr.Ahmed");
        course3.setCredit(3.0);
        courseRepo.save(course3);

        Course course4 = new Course();
        course4.setCourseCode("Python001");
        course4.setInstructor("TBA");
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
    public String poststudenttocourse(@PathVariable("id") long courseId, @RequestParam(value="studentsIds", required=false) Long[] checkedstudentsId,
                                      @ModelAttribute ("allstudents") ArrayList<Person> allstudent,  Model model)
    {

        Course newCourse= courseRepo.findOne(courseId);

//        System.out.println("Step1=========");
//
//        System.out.println(checkedstudentsId[0]);

        if(checkedstudentsId.length!=0) {

            for (long studentId : checkedstudentsId) {
                System.out.println("+++++++++" + studentId);
                newCourse.addPersontoCourse(personRepo.findOne(studentId));

            }
            courseRepo.save(newCourse);

        }

        long courseToGoTo = newCourse.getCourseId();

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

        return "redirect:/onecourselist/" + courseId;
    }

    @GetMapping("/onecourselist/{id}")
    public String listOnecourse(@PathVariable("id") long courseId, Model model)
    {
        model.addAttribute("course", courseRepo.findOne(courseId));
        return "onecourselist";

    }



    @GetMapping("/listallcourses")
    public String listcourse(Model model)
    {
        model.addAttribute("allcourses", courseRepo.findAll());
        return "listallcourses";
    }


//    @GetMapping("/addperson")
//    public String addPersonandHomepage(Model model)
//    {
//
//        Person person = new Person();
//        System.out.println(person.getUuid());
//        model.addAttribute("newperson", person);
//        return "addperson";
//    }

    //take person result, display result and allow user to add edu/skill/exp to this person
//    @PostMapping("/displayperson")
//    public String postperson(@Valid @ModelAttribute("newperson") Person person, BindingResult bindingResult)
//    {
//        if(bindingResult.hasErrors())
//        {
//            return "addperson";
//        }
//
//        System.out.println("when postMapping form, will the auto generated ID show up:    " +person.getUuid());
//
//        personRepo.save(person);
//
//
//        //yes the ID show up here!!!!!!!!!!!!!
//        System.out.println("After save the person to database, will the auto generated ID show up:    " +person.getUuid());
//
//        return "displayperson";
//    }

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
//    @GetMapping("/addeducationtoperson/{id}")
//    public String addEduToPerson(@PathVariable("id") long personId, Model model){
//
//        System.out.println("!!!!!" + personId);
//
////        String filename = uploadResult.get("public_id").toString() + "." + uploadResult.get("format").toString();
////        model.addAttribute("sizedimageurl", cloudc.createUrl(filename,100,150, "fit"));
////        shoppingCart.addThings(filename);
////        model.addAttribute("filesuploaded", shoppingCart.getThings());
//
////        long num = 3L;
////        String t=  Long.toString(num);
////
////        int numint = 3;
////        String y = Integer.toString(numint);
//
//
//        // this works.successfully print out the person ID
////        personIdReuse.setPersonId(personId);
////        System.out.println("===testing session variable:   " + personIdReuse.getPersonId());
//
//
//        //try use this personIdReuse in Html print out
//
//        model.addAttribute("tryprintout", personIdReuse.getPersonId());
//
//
//        Person oneperson= personRepo.findOne(personId);
////
////      personIdReuse.addThings(Long.toString(oneperson.getUuid()));
//
//
//        model.addAttribute("oneperson", oneperson);
//
//        Education education = new Education();
//        //try make an connection with education and person, doesn't work, it has to be right beofore save,
//        // the same code in posting works when save the education right after!!!
//        //education.setPerson(oneperson);
//        //System.out.println(education.getPerson().getUuid());
//        model.addAttribute("neweducation", education);
//        return "addedutopersonform";
//    }

    @GetMapping("/addeducationtoperson")
    public String addedutopersonpricipal(Principal p, Model model)
    {
        Person oneperson= personRepo.findByUsername(p.getName());
        model.addAttribute("oneperson", oneperson);

        Education education = new Education();
        model.addAttribute("neweducation", education);
        return "addedutopersonformP";

    }

    @PostMapping("/addeducationtoperson")
    public String postEdutoPersonP(Principal p,  @ModelAttribute("neweducation") Education education, BindingResult bindingResult, Model model) {


        if (bindingResult.hasErrors()) {
            return "addedutopersonformP";
        }

        Person oneperson = personRepo.findByUsername(p.getName());

        education.setPerson(oneperson);

        //try add it to model and see if it can be printed in HTML

        educationRepo.save(education);
        return "displayeducation";
    }




//
//        @PostMapping("/addeducationtoperson/{personid}")
//    public String postEdutoPerson(@PathVariable("personid") long personId,  @ModelAttribute("neweducation") Education education, BindingResult bindingResult, Model model){
//
//
//        if(bindingResult.hasErrors())
//        {
//            return "/addeducationtoperson/{id}";
//        }
//
//        System.out.println("==== personID:   " + personId);
//
//        //this works as well, so it can be used in another method as a variable
//        System.out.println("===testing session variable:   " + personIdReuse.getPersonId());
//
//        //personRepo.findOne(personId).addEdu(education); this doesn't create the relationship, maybe because person is being mapped!!
//        //try the following
//        education.setPerson(personRepo.findOne(personId));
//
//        //try add it to model and see if it can be printed in HTML
//        model.addAttribute(personIdReuse.getPersonId());
//
//        educationRepo.save(education);
//        return "displayeducation";
//    }

    ///add skill to a person method works
//    @GetMapping("/addskilltoperson/{id}")
//    public String addSkillToPerson(@PathVariable("id") long personId, Model model){
//
//        System.out.println("!!!!!" + personId);
//
//        Person oneperson= personRepo.findOne(personId);
//        model.addAttribute("oneperson", oneperson);
//
//        Skill skill = new Skill();
//        model.addAttribute("newskill", skill);
//
//        return "addskilltopersonform";
//    }
//
//    @PostMapping("/addskilltoperson/{personid}")
//    public String postskilltoPerson(@PathVariable("personid") long personId,  @ModelAttribute("newskill") Skill skill, BindingResult bindingResult, Model model){
//
//        if(bindingResult.hasErrors())
//        {
//            return "/addskilltoperson/{personid}";
//        }
//
//        System.out.println("==== personID:   " + personId);
//
//        //personRepo.findOne(personId).addEdu(education); this doesn't create the relationship, maybe because person is being mapped!!
//        //try the following
//        skill.setPerson(personRepo.findOne(personId));
//        skillRepo.save(skill);
//        return "displayskill";
//    }


    @GetMapping("/addexptoperson")
    public String addExpToPerson(Principal p, Model model) {


        Person oneperson = personRepo.findByUsername(p.getName());
        model.addAttribute("oneperson", oneperson);

        Experience experience = new Experience();
        model.addAttribute("newexperience", experience);

        return "addexptopersonformP";
    }


//    //add exptoperson get and post mapping methods!!!  addexptoperson/
//    @GetMapping("/addexptoperson/{id}")
//    public String addExpToPerson(@PathVariable("id") long personId, Model model){
//
//        System.out.println("!!!!!" + personId);
//
//        Person oneperson= personRepo.findOne(personId);
//        model.addAttribute("oneperson", oneperson);
//
//        Experience experience = new Experience();
//        model.addAttribute("newexperience", experience);
//
//        return "addexptopersonform";
//    }

    @PostMapping("/addexptoperson")
    public String postExptoPerson(Principal p,  @ModelAttribute("newexperience") Experience experience, BindingResult bindingResult, Model model){

        if(bindingResult.hasErrors())
        {
            return "addexptopersonformP";
        }

        //personRepo.findOne(personId).addEdu(education); this doesn't create the relationship, maybe because person is being mapped!!
        //try the following
        experience.setPerson(personRepo.findByUsername(p.getName()));
        experienceRepo.save(experience);
        return "displayexperience";
    }


//    @PostMapping("/addexptoperson/{personid}")
//    public String postExptoPerson(@PathVariable("personid") long personId,  @ModelAttribute("newexperience") Experience experience, BindingResult bindingResult, Model model){
//
//        if(bindingResult.hasErrors())
//        {
//            return "addexptopersonform";
//        }
//
//        System.out.println("==== personID:   " + personId);
//
//        //personRepo.findOne(personId).addEdu(education); this doesn't create the relationship, maybe because person is being mapped!!
//        //try the following
//        experience.setPerson(personRepo.findOne(personId));
//        experienceRepo.save(experience);
//        return "displayexperience";
//    }

    @GetMapping("/displayoneprofile")
    public String displayOneProfile(Principal p, Model model)
    {


        model.addAttribute("newperson", personRepo.findByUsername(p.getName()));

        return "displayoneprofile";
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
        return "addedutopersonformP";
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
//    @RequestMapping("/updatesk/{id}")
//    public String updatesk(@PathVariable("id") long skillId, Model model){
//
//        Skill updateskill = skillRepo.findOne(skillId);
//
//        Person oneperson = updateskill.getPerson();
//
//        model.addAttribute("oneperson", oneperson);
//
//        model.addAttribute("newskill", updateskill);
//        return "addskilltopersonform";
//    }
//
//    // remove skills, same as edu, remove from person first!!!!!
//    @RequestMapping("/deletesk/{id}")
//    public String delsk(@PathVariable("id") long skillId){
//
//
//        Skill deleteskil = skillRepo.findOne(skillId);
//        Person oneperson = deleteskil.getPerson();
//        long personToGoTo = oneperson.getUuid();
//
//        oneperson.removeSkl(deleteskil);
//
//        skillRepo.delete(skillId);
//        return "redirect:/displayoneprofile/" + personToGoTo;
//    }

    @RequestMapping("/updateexp/{id}")
    public String updateexp(@PathVariable("id") long experienceId, Model model){

        Experience updateexp = experienceRepo.findOne(experienceId);

        Person oneperson = updateexp.getPerson();

        model.addAttribute("oneperson", oneperson);

        model.addAttribute("newexperience", updateexp);

        return "addexptopersonformP";
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
