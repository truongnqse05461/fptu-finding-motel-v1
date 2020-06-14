package com.example.fptufindingmotelv1.controller;

import com.example.fptufindingmotelv1.model.UserModel;
import org.hibernate.LazyInitializationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.security.Principal;

@Controller
public class HomeController {

    @GetMapping("/")
    public String getHomepage(Model model){
        return "index";
    }
    @GetMapping("/dang-nhap")
    public String getLogin(Model model){
        return "login";
    }
    @GetMapping("/dang-ki-with-gg")
    public String getRegisterwithgg(Model model){
        UserModel userModel = new UserModel();
        userModel.setDisplayName("truong");
        model.addAttribute("userModel", userModel);
        return "register-social";
    }

    @ResponseBody
    @GetMapping("/renter")
    public Principal getPrincipal (Principal principal) throws LazyInitializationException {

        return principal;
    }
}
