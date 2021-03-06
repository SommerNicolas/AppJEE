package app.controllers;

import app.forms.CreateForm;
import app.models.Project;
import app.models.User;
import app.services.NotificationService;
import app.services.ProjectService;
import app.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by mega- on 22.04.2017.
 */
@Controller
public class CreateController {
    @Autowired
    private ProjectService projectService;

    @Autowired
    private UserService userService;

    @Autowired
    private NotificationService notifyService;

    @RequestMapping("/projects/create")
    public String create(CreateForm createForm, Model model){
        User currentUser = userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        if(currentUser == null){
            model.addAttribute("islog", false);
        }
        else{
            model.addAttribute("islog", true);
        }
        return "projects/create";
    }

    @RequestMapping(value = "/projects/create", method = RequestMethod.POST)
    public String createPage(@Valid CreateForm createForm, BindingResult bindingResult, Model model) {
        User currentUser = userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        if(currentUser == null){
            model.addAttribute("islog", false);
        }
        else{
            model.addAttribute("islog", true);
        }

        if (bindingResult.hasErrors()) {
            notifyService.addErrorMessage("Please fill the form correctly!");
            return "projects/projects";
        }
        User current = userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        String title = createForm.getTitle();
        List<Project> project = projectService.findByName(title);
        if(project.isEmpty()){
            Project p = new Project(createForm.getTitle(), createForm.getDescription(), current);
            projectService.create(p);
        }
        else{
            notifyService.addErrorMessage("This name is already used");
            return "projects/projects";
        }


        notifyService.addInfoMessage("Project created");
        return "redirect:/projects/projects";
    }
}
