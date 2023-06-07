package com.raffle.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ImportTicketsController {

    @RequestMapping("import-tickets")
    public String addTicketsFile() {

        return "import-tickets";

    }

}
