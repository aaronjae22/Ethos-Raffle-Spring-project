package com.raffle.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AddTicketsFileController {

    @RequestMapping("add-tickets-file")
    public String addTicketsFile() {

        return "add-tickets-file";

    }

}
