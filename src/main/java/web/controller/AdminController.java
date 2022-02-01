package web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import web.model.Role;
import web.model.User;
import web.service.RoleService;
import web.service.UserService;

import java.security.Principal;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping(value = "")
    public ModelAndView allUsers(ModelAndView modelAndView,
                                 Principal principal) {
        User userIn = userService.findByUsername(principal.getName());
        List<User> userList = userService.listUsers();
        List<Role> roles = roleService.getRolesList();
        modelAndView.addObject("roles", roles);
        modelAndView.addObject("userIn", userIn);
        modelAndView.addObject("user", new User());
        modelAndView.addObject("userList", userList);
        modelAndView.setViewName("adminPage");
        return modelAndView;
    }

    @PostMapping(value = "/addUser")
    public ModelAndView addUser(@Validated(User.class)
                                @ModelAttribute("user") User user,
                                @RequestParam("authorities") List<String> preRoles,
                                ModelAndView modelAndView) {
        modelAndView.setViewName("redirect:/admin");
        Set<Role> roles = roleService.getSetRoles(preRoles);
        user.setRoles(roles);
        userService.registerUser(user);
        return modelAndView;
    }

    @PostMapping(value = "/editUser")
    public ModelAndView editUser(@Validated(User.class)
                                 @ModelAttribute("user") User user,
                                 @RequestParam("authorities") List<String> preRoles,
                                 @RequestBody String body, // for test
                                 ModelAndView modelAndView) {
        modelAndView.setViewName("redirect:/admin");
        Set<Role> roles = roleService.getSetRoles(preRoles);
        user.setRoles(roles);
        userService.editUser(user);
        return modelAndView;
    }

    @GetMapping(value = "/delete/{id}")
    public ModelAndView deleteUser(@PathVariable("id") int id, ModelAndView modelAndView) {
        modelAndView.setViewName("redirect:/admin");
        User user = userService.getUserById(id);
        userService.deleteUser(user);
        return modelAndView;
    }


}
