package app.controllers;

import app.forms.RegisterForm;
import app.models.User;
import app.services.NotificationService;
import app.services.SecurityService;
import app.services.UserService;
import app.validator.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

@Controller
public class RegisterController {
    @Autowired
    private UserService userService;

    @Autowired
    private SecurityService securityService;

    @Autowired
    private UserValidator userValidator;

    @Autowired
    private NotificationService notifyService;

    @RequestMapping(value = "/users/register", method = RequestMethod.GET)
    public String registration(Model model) {
        User currentUser = userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        if(currentUser == null){
            model.addAttribute("islog", false);
        }
        else{
            model.addAttribute("islog", true);
        }

        model.addAttribute("registerForm", new User());
        return "users/register";
    }

    @RequestMapping(value = "/users/register", method = RequestMethod.POST)
    public String registration(@ModelAttribute("registerForm") User userForm, BindingResult bindingResult, Model model) {
        User currentUser = userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        if(currentUser == null){
            model.addAttribute("islog", false);
        }
        else{
            model.addAttribute("islog", true);
        }

        userService.save(userForm);

        securityService.autologin(userForm.getUsername(), userForm.getPasswordHash());

        return "redirect:/";
    }
}
