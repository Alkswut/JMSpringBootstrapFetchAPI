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

    @GetMapping("")
    public ModelAndView adminPage(ModelAndView modelAndView, Principal principal) {
        User user = userService.findByUsername(principal.getName());
        modelAndView.addObject("user", user);
        modelAndView.setViewName("admin");
        return modelAndView;
    }

    @GetMapping(value = "/users")
    public ModelAndView allUsers(ModelAndView modelAndView) {
        List<User> userList = userService.listUsers();
        modelAndView.setViewName("users");
        modelAndView.addObject("userList", userList);
        return modelAndView;
    }

    @GetMapping(value = "/add")
    public ModelAndView add(ModelAndView modelAndView) {
        User user = new User();
        List<Role> roles = roleService.getRolesList();
        modelAndView.addObject("roles", roles);
        modelAndView.addObject("user", user);
        modelAndView.setViewName("addUser");
        return modelAndView;
    }

    @PostMapping(value = "/addUser")
    public ModelAndView addUser(@Validated(User.class)
                                @ModelAttribute("user") User user,
                                @RequestParam("authorities") List<String> preRoles,
                                ModelAndView modelAndView) {
        modelAndView.setViewName("redirect:/admin/users");
        Set<Role> roles = roleService.getSetRoles(preRoles);
        user.setRoles(roles);
        userService.registerUser(user);
        return modelAndView;
    }

    @GetMapping(value = "/edit/{id}")
    public ModelAndView edit(@PathVariable("id") int id, ModelAndView modelAndView) {
        User user = userService.getUserById(id);
        List<Role> roles = roleService.getRolesList();
        modelAndView.addObject("roles", roles);
        modelAndView.addObject("user", user);
        modelAndView.setViewName("editUser");
        return modelAndView;
    }

    @PostMapping(value = "/editUser")
    public ModelAndView editUser(@Validated(User.class)
                                 @ModelAttribute("user") User user,
                                 @RequestParam("authorities") List<String> preRoles,
                                 @RequestBody String body, // for test
                                 ModelAndView modelAndView) {
        modelAndView.setViewName("redirect:/admin/users");
        Set<Role> roles = roleService.getSetRoles(preRoles);
        user.setRoles(roles);
        userService.editUser(user);
        return modelAndView;
    }

    @GetMapping(value = "/delete/{id}")
    public ModelAndView deleteUser(@PathVariable("id") int id, ModelAndView modelAndView) {
        modelAndView.setViewName("redirect:/admin/users");
        User user = userService.getUserById(id);
        userService.deleteUser(user);
        return modelAndView;
    }
}
