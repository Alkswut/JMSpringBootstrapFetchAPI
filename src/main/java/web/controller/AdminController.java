package web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
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

@RestController
@RequestMapping(value = "/admin")
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
        modelAndView.addObject("userIn", userIn);
        modelAndView.addObject("roles", roles);
        modelAndView.addObject("user", new User());
        modelAndView.addObject("userList", userList);
        modelAndView.setViewName("adminPage");
        return modelAndView;
    }

    @GetMapping(value = "/getListUsers")
    public ResponseEntity<List<User>> getListUsers() {
        List<User> userList = userService.listUsers();
        return userList != null && !userList.isEmpty()
                ? new ResponseEntity<>(userList, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping(value = "/getUserById/{id}")
    public ResponseEntity<User> getUserById(@PathVariable("id") Long id) {
        User user = userService.getUserById(id);
        return user != null
                ? new ResponseEntity<>(user, HttpStatus.OK)
                : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping(value = "/addUser")
    public ResponseEntity<User> addUser(@RequestBody User user) {
        userService.registerUser(user);
        return ResponseEntity.ok().body(user);
    }

    @PutMapping(value = "/userEdit")
    public ResponseEntity<User> editUser(@RequestBody User user) {
        userService.editUser(user);
        return ResponseEntity.ok().body(user);
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<User> deleteUser(@PathVariable("id") Long id) {
        User user = userService.getUserById(id);
        userService.deleteUser(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
