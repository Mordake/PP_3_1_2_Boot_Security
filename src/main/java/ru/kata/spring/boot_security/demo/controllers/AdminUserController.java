package ru.kata.spring.boot_security.demo.controllers;

import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.User;

@Controller
@RequestMapping("/admin")
public class AdminUserController {

    private final ru.kata.spring.boot_security.demo.service.UserService userService;

    public AdminUserController(ru.kata.spring.boot_security.demo.service.UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public String getAllUser(Model model) {
        model.addAttribute("users", userService.findAll());
        model.addAttribute("user", new User());
        return "users";

    }

    @PostMapping("/add")
    public String addUser(@Valid @ModelAttribute User user, BindingResult bindingResult, Model model) {
        System.out.println("В методе addUser");
        if (bindingResult.hasErrors()) {
            bindingResult.getAllErrors().forEach(error -> System.out.println(error.getDefaultMessage()));
            return "users";
        }



        try {
            userService.save(user);
            System.out.println("добавлен");
            model.addAttribute("users", userService.findAll());
            return "redirect:/admin/users";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "users";
        }
    }

    @PostMapping("/update")
    public String updateUser(@Valid @ModelAttribute User user,
                             BindingResult bindingResult,
                             Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("users", userService.findAll());
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
        return "users";
    }
}
