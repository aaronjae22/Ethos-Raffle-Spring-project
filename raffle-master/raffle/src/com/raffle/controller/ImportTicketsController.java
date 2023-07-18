package com.raffle.controller;

import java.io.*;
import java.nio.file.Files;
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
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.raffle.dao.UserDataAccess;
import com.raffle.pojo.User;
import org.springframework.web.multipart.MultipartFile;

@Controller
class ImportTicketsController extends BaseController {

    @Autowired
    UserDataAccess userDataAccess;

    @Autowired
    ImportTicketsHelper importTicketsHelper;

    public MultipartFile fileUploaded;

    @Transactional
    @Secured("Administrator")
    @RequestMapping(value = "import-tickets")
    public String importTickets(Model model) throws IOException {
        if (this.hasRole("Administrator")) {

            System.out.println("User has permission.");

            /* Path path = Paths.get("C:\\Usehttps://www.logitech.com/en-us/products/mice/mx-master-3s-mac-bluetooth-mouse.910-006570.html?searchclick=logirs\\ayerd\\Documents\\Laboral\\EthosApps\\Projects\\Independents\\Raffle-Spring\\raffle-master\\raffle\\datafiles\\Excel_Import.xlsx");

            List<TicketNumbers> importedTickets = ImportTicketsHelper.importTickets(path);
            importTicketsHelper.insertImportedTickets(importedTickets);
            model.addAttribute("ticketList", importedTickets); */

            /* WORKING. DELETE COMMENTS
            List<BundleDetails> bundleImportedTickets = ImportTicketsHelper.bundleImportTickets(path);
            // System.out.println(bundleImportedTickets);
            importTicketsHelper.getTicketsFromImportedBundle(bundleImportedTickets); */

        } else {
            System.out.println("It doesn't have permission");
        }
        return "import-tickets";
    }

    @PostMapping("/upload")
    public ResponseEntity<?> handleFileUpload(@RequestParam("file") MultipartFile file) throws IOException {

        String fileName = file.getOriginalFilename();

        File convertedFile;

        System.out.println("File Name: " + fileName);

        System.out.println("File Content Type" + file.getContentType());

        fileUploaded = file;

        System.out.println("File Uploaded: " + fileUploaded);

        // Error  occurs here
        convertedFile = convertMultiFileToFile(file);

        System.out.println("Converted File: " + convertedFile);
        // System.out.println("Converted File Type: " + convertedFile.getClass().getName());

        return ResponseEntity.ok("File Uploaded !!! : " + fileName);
    }

    public File convertMultiFileToFile(MultipartFile file) throws IOException {

        File convFile = new File(file.getOriginalFilename());

        convFile.createNewFile();

        try(InputStream is = file.getInputStream()) {
            // Files.copy(is, convFile.toPath());
        }

        return convFile;

    }


    /* public void getSingleTicketsFromFile() throws IOException {

        List<TicketNumbers> importedTickets = ImportTicketsHelper.importTickets(fileUploaded);
        importTicketsHelper.insertImportedTickets(importedTickets);

    } */


}
