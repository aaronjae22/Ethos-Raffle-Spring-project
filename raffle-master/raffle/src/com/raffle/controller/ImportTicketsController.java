package com.raffle.controller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.raffle.ImportTicketsHelper;
import com.raffle.pojo.BundleDetails;
import com.raffle.pojo.TicketNumbers;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.raffle.dao.UserDataAccess;
import com.raffle.pojo.User;


@Controller
class ImportTicketsController extends BaseController {

    @Autowired
    UserDataAccess userDataAccess;

    @Autowired
    ImportTicketsHelper importTicketsHelper;

    @Transactional
    @Secured("Administrator")
    @RequestMapping(value = "import-tickets")
    public String importTickets(Model model) throws IOException {
        if (this.hasRole("Administrator")) {
            Path path = Paths.get("C:\\Users\\ayerd\\Documents\\Laboral\\EthosApps\\Projects\\Independents\\Raffle-Spring\\raffle-master\\raffle\\datafiles\\Excel_Import.xlsx");

            List<TicketNumbers> importedTickets = ImportTicketsHelper.importTickets(path);
            // System.out.println(importedTickets);
            importTicketsHelper.insertImportedTickets(importedTickets);
            model.addAttribute("ticketList", importedTickets);

            List<BundleDetails> bundleImportedTickets = ImportTicketsHelper.bundleImportTickets(path);
            // System.out.println(bundleImportedTickets);
            importTicketsHelper.getTicketsFromImportedBundle(bundleImportedTickets);

        } else {
            System.out.println("It doesn't have permission");
        }
        return "import-tickets";
    }



}
