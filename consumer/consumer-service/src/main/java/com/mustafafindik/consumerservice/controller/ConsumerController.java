package com.mustafafindik.consumerservice.controller;

import com.mustafafindik.consumerservice.service.UserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/consumer")
public class ConsumerController {

    private final UserService userService;

    public ConsumerController(UserService userService) {
        this.userService = userService;
    }

}
