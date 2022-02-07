package web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import web.model.User;
import web.service.UserService;

import java.security.Principal;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("")
    public ModelAndView userPage(ModelAndView modelAndView, Principal principal) {
        User userIn = userService.findByUsername(principal.getName());
        modelAndView.addObject("userIn", userIn);
        modelAndView.setViewName("userPage");
        return modelAndView;
    }

    @GetMapping("/getUser")
    public ResponseEntity<?> getUser(Principal principal) {
        User user = userService.findByUsername(principal.getName());
        return ResponseEntity.ok().body(user);
    }
}
