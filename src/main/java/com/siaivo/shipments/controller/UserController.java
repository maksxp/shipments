package com.siaivo.shipments.controller;

import com.siaivo.shipments.model.User;
import com.siaivo.shipments.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import javax.validation.Valid;

@Controller
public class UserController {
    @Autowired
    private UserService userService;
//    @Autowired
//    private CompanyService companyService;

    @RequestMapping(value="/admin/allUsers", method = RequestMethod.GET)
    public ModelAndView allUsers(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("allUsers", userService.allUsers());
        modelAndView.setViewName("/admin/allUsers");
        return modelAndView;
    }

    @RequestMapping(value="/admin/userRegistration", method = RequestMethod.GET)
    public ModelAndView createNewUser(){
        ModelAndView modelAndView = new ModelAndView();
        User user = new User();
        modelAndView.addObject("user", user);
        modelAndView.setViewName("/admin/userRegistration");
        return modelAndView;
    }

    @RequestMapping(value = "/admin/userRegistration", method = RequestMethod.POST)
    public ModelAndView createNewUser(@Valid User user, BindingResult bindingResult) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("allUsers", userService.allUsers());
        User userExists;
        userExists = userService.findUserByEmail(user.getEmail());
        if (userExists != null) {
            bindingResult
                    .rejectValue("email", "error.user",
                            "Користувач з такими email вже зареєстрований");
        }
        if (bindingResult.hasErrors()) {
           modelAndView.setViewName("/admin/userRegistration");
        } else {
            userService.saveUser(user);
            modelAndView.addObject("successMessage", "Користувача успішно додано");
            modelAndView.addObject("user", new User());
            modelAndView.setViewName("/admin/userRegistration");

        }
        return modelAndView;
    }
    @RequestMapping(value = "/admin/userEdit/{id}", method = RequestMethod.GET)
    public ModelAndView editUser(@PathVariable(value = "id") int id){
        ModelAndView modelAndView = new ModelAndView();
        User user =  userService.findUserById(id);
        modelAndView.addObject("user", user);
        modelAndView.setViewName("admin/userEdit");
        return modelAndView;
    }

    @RequestMapping(value = "/admin/userEdit", method = RequestMethod.POST)
    public ModelAndView editUser(@ModelAttribute("user")User user) {
        ModelAndView modelAndView = new ModelAndView();
        String type =  user.getUserType();
        String password =  user.getPassword();
        String name =  user.getName();
        String email = user.getEmail();
        user = userService.findUserByEmail(email);
        int id = user.getId();
        //userType changing
        userService.editUserType(type, id);
        //name changing
        if (name.length()<1 ) {
            modelAndView.addObject("successMessage", "Ім'я не може бути пустим");
            return modelAndView;
        } else {
        userService.editUserName(name, id);
        }
        //password changing
        if (password.length()<=4 ){
            modelAndView.addObject("successMessage", "Пароль має бути не менше п'яти символів");
            return modelAndView;
        } else {
        userService.editUserPassword(password, id);
        }
        userService.saveUser(user);
        modelAndView.addObject("successMessage", "Зміни успішно внесено");
        return modelAndView;
    }
}
