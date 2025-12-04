package ru.kata.spring.boot_security.demo.controllers;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.service.RoleService;

@Controller
@RequestMapping("/admin")
public class AdminUserController {

    private final ru.kata.spring.boot_security.demo.service.UserService userService;
    private final RoleService roleService;

    public AdminUserController(ru.kata.spring.boot_security.demo.service.UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("/users")
    public String getAllUser(Model model) {
        model.addAttribute("users", userService.findAll());
        model.addAttribute("user", new User());
        model.addAttribute("roles", roleService.findAll());
        return "users";

    }

    @PostMapping("/add")
    public String addUser(@Valid @ModelAttribute User user, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("users", userService.findAll());
            model.addAttribute("roles", roleService.findAll()); // ← ДОБАВИТЬ
            return "users";
        }
        userService.save(user);
        return "redirect:/admin/users";
    }

    @PostMapping("/update")
    public String updateUser(@Valid @ModelAttribute User user,
                             BindingResult bindingResult,
                             Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("users", userService.findAll());
            model.addAttribute("roles", roleService.findAll());
            return "users";
        }
        try {
            userService.update(user);
            return "redirect:/admin/users";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("users", userService.findAll());
            return "users";
        }
    }

    @PostMapping("/delete")
    public String deleteUser(@RequestParam("id") Long id) {
        userService.delete(id);
        return "redirect:/admin/users";
    }

    @PostMapping("/edit")
    public String editUser(@RequestParam("id") Long id, Model model) {
        User user = userService.findById(id);
        model.addAttribute("user", user);
        model.addAttribute("users", userService.findAll());
        model.addAttribute("roles", roleService.findAll());
        return "users";
    }
}
