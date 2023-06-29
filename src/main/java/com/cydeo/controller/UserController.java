package com.cydeo.controller;

import com.cydeo.dto.UserDto;
import com.cydeo.service.CompanyService;
import com.cydeo.service.RoleService;
import com.cydeo.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;
    private final RoleService roleService;
    private final CompanyService companyService;

    public UserController(UserService userService, RoleService roleService, CompanyService companyService) {
        this.userService = userService;
        this.roleService = roleService;
        this.companyService = companyService;
    }


    @GetMapping("/list")
    public String listUsers(Model model) {
        model.addAttribute("users", userService.listAllUsers());
        return "/user/user-list";
    }

    @GetMapping("/update/{id}")
    public String editUser(@PathVariable("id") Long id, Model model) {

        model.addAttribute("user", userService.findById(id));
        model.addAttribute("userRoles", roleService.listAllRoles());
        model.addAttribute("companies", companyService.listAllCompanies());
        return "/user/user-update";
    }

    @PostMapping("/update/{id}")
    public String updateUser(@PathVariable("id") Long id, @ModelAttribute("user") UserDto user, Model model) {

        model.addAttribute("user", userService.findById(id));
        model.addAttribute("userRoles", roleService.listAllRoles());
        model.addAttribute("companies", companyService.listAllCompanies());

        userService.update(user);
        return "redirect:/users/list";
    }

    @GetMapping("/create")
    public String createUser(Model model) {

        model.addAttribute("newUser", new UserDto());
        model.addAttribute("userRoles", roleService.listAllRoles());
        model.addAttribute("companies", companyService.listAllCompanies());

        return "user/user-create";
    }

    @PostMapping("/create")
    public String saveUser(@ModelAttribute("newUser") UserDto user, Model model) {

        model.addAttribute("userRoles", roleService.listAllRoles());
        model.addAttribute("companies", companyService.listAllCompanies());

        userService.save(user);


        return "redirect:/users/list";
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id) {

        userService.delete(id);

        return "redirect:/users/list";
    }


}