package com.raffle.controller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.raffle.ImportTicketsHelper;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.raffle.dao.UserDataAccess;
import com.raffle.pojo.User;

@Controller
class ImportTicketsController extends BaseController {

    @Autowired
    UserDataAccess userDataAccess;

    @Secured("Administrator")
    @RequestMapping(value = "import-tickets")
    String importTickets() throws IOException {

        if (this.hasRole("Administrator")) {
            Path path = Paths.get("C:\\Users\\ayerd\\Documents\\Laboral\\EthosApps\\Projects\\Independents\\Raffle-Spring\\raffle-master\\raffle\\datafiles\\Excel_Import.xlsx");
            ImportTicketsHelper.importTickets(path);
        } else {
            System.out.println("It doesn't have permission");
        }


        return "import-tickets";
    }

}
