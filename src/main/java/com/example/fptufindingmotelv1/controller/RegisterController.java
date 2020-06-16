package com.example.fptufindingmotelv1.controller;

import com.example.fptufindingmotelv1.dto.UserDTO;
import com.example.fptufindingmotelv1.repository.UserRepository;
import com.example.fptufindingmotelv1.service.register.RegisterService;
import net.minidev.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class RegisterController {

    @Autowired
    private RegisterService registerService;
    @Autowired
    private UserRepository userRepository;

    @GetMapping("/dang-ki")
    public String getRegister() {
        return "register";
    }

    @ResponseBody
    @PostMapping("/validRegister")
    public String validateRegister(Model model, @RequestBody UserDTO userDTO) {
        JSONObject registerMsg = new JSONObject();
        if (registerService.save(userDTO).getUsername() != null) {
            registerMsg.put("code", "0");
            registerMsg.put("message", "Register success!");
        }
        return registerMsg.toJSONString();
    }

    @ResponseBody
    @RequestMapping(value = "/isExistUsername", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public Boolean isExistUsername(@RequestBody String username) {
        return userRepository.existsByUsername(username);
    }

    @ResponseBody
    @RequestMapping(value = "/isExistPhone", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public Boolean isExistPhone(@RequestBody String phone) {
        return userRepository.existsByPhoneNumber(phone);
    }


    @ResponseBody
    @RequestMapping(value = "/api/get-otp", method = RequestMethod.POST, produces = MediaType.APPLICATION_JSON_VALUE)
    public char[] getOTP(@RequestParam int otpLength) {
        return registerService.generateOTP(otpLength);
    }
}



