package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.services.UserServiceImpl;
import ru.kata.spring.boot_security.demo.util.UserValidator;

import javax.validation.Valid;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserServiceImpl userServiceImpl;
    private final UserValidator userValidator;

    @Autowired
    public AdminController(UserServiceImpl userServiceImpl, UserValidator userValidator) {
        this.userServiceImpl = userServiceImpl;
        this.userValidator = userValidator;
    }

    @GetMapping()
    public String showUserList(Model model) {
        model.addAttribute("users", userServiceImpl.getAllUsers());
        return "admin";
    }

    @GetMapping("/new")
    public String addNewUser(@ModelAttribute("user") User user) {
        return "new";
    }

    @PostMapping()
    public String addUser(@ModelAttribute("user") @Valid User user, BindingResult bindingResult) {
        userValidator.validate(user, bindingResult);
        if (bindingResult.hasErrors()) {
            return "new";
        }
        userServiceImpl.addUser(user);
        return "redirect:/admin";
    }

    @GetMapping("/{id}/edit")
    public String editUser(Model model, @PathVariable("id") Long id) {
        model.addAttribute("user", userServiceImpl.getUserById(id));
        return "edit";
    }

    @PostMapping("/{id}")
    public String updateUser(@ModelAttribute("user") @Valid User user, BindingResult bindingResult,
                             @PathVariable("id") Long id) {
        userValidator.validate(user, bindingResult);
        if (bindingResult.hasErrors()) {
            return "edit";
        }
        userServiceImpl.updateUser(id, user);
        return "redirect:/admin";
    }

    @PostMapping("/{id}/delete")
    public String deleteUser(@PathVariable("id") Long id) {
        userServiceImpl.deleteUser(id);
        return "redirect:/admin";
    }

    // Новые маппинги для смены пароля (доступны только администратору)

    // GET: Отображение формы для смены пароля выбранного пользователя
    @GetMapping("/{id}/change-password")
    public String showChangePasswordForm(@PathVariable("id") Long id, Model model) {
        User user = userServiceImpl.getUserById(id);
        model.addAttribute("user", user);
        return "change-password"; // Страница change-password.html должна содержать форму ввода нового пароля
    }

    // POST: Обработка запроса на смену пароля
    @PostMapping("/{id}/change-password")
    public String changePassword(@PathVariable("id") Long id,
                                 @RequestParam("newPassword") String newPassword) {
        userServiceImpl.changePassword(id, newPassword);
        return "redirect:/admin?passwordChanged"; // При успешной смене пароля можно передать параметр для уведомления
    }
}
