package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.UserServiceImpl;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("admin")
public class UserController {

    private final UserServiceImpl userService;

    @Autowired
    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @GetMapping
    public String users(Model model) {
        model.addAttribute("users", userService.listUsers());
        return "userss";
    }

    @GetMapping("create")
    public String createUserForm(User user, Model model) {
        model.addAttribute("roleList", userService.listRoles());
        return "create";
    }

    @PostMapping("create")
    public String createUser(/*@ModelAttribute("user") */User user) {
        List<String> lsr = new ArrayList<>();
        for (Role r : user.getRoles()) {
            Object role = r.getRole();
            lsr.add((String) role);
        }
        List<Role> liRo = userService.listByRole(lsr);
        user.setRoles((Set<Role>) liRo);
        userService.addUser(user);
        return "redirect:/admin";
    }

    @GetMapping("delete/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        userService.delete(id);
        return "redirect:/admin";
    }

    @GetMapping("update/{id}")
    public String updateUserForm(@PathVariable("id") Long id, Model model) {
        User user = userService.findById(id);
        model.addAttribute("user", user);
        model.addAttribute("roleList", userService.listRoles());
        return "update";
    }

    @PostMapping("update")
    public String updateUser(User user) {
        List<String> lsr = new ArrayList<>();
        for (Role r : user.getRoles()) {
            Object role = r.getRole();
            lsr.add((String) role);
        }
        List<Role> liRo = userService.listByRole(lsr);
        user.setRoles((Set<Role>) liRo);
        userService.updateUser(user);
        return "redirect:/admin";
    }
}
