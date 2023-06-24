package com.raffle;

import com.raffle.pojo.BundleDetails;
import com.raffle.pojo.Ticket;
import com.raffle.pojo.TicketNumbers;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import com.raffle.dao.ImportTicketsDataAccess;
import org.springframework.stereotype.Component;

@Component
public class ImportTicketsHelper {

    private static final int BUNLDE_INDEX = 0;

    @Autowired
    ImportTicketsDataAccess importTicketsDataAccess;

    private static XSSFSheet getSheet(Path path, String sheetName) throws IOException {
        String excelFilePath = String.valueOf(path);
        FileInputStream inputStream = new FileInputStream(excelFilePath);
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);

        return workbook.getSheet(sheetName);
    }


    public static List<TicketNumbers> importTickets(Path path) throws IOException {

        XSSFSheet sheet = getSheet(path, "Tickets");

        Iterator<Row> rows = sheet.iterator();
        List<TicketNumbers> ticketList = new ArrayList<>();

        while(rows.hasNext()) {

            Row row = rows.next();
            if(row.getRowNum() < 2) continue;

            Iterator<Cell> cellIterator  = row.cellIterator();

            while(cellIterator.hasNext()) {
                TicketNumbers ticket = new TicketNumbers();
                ticket.setIdPeriod(12);

                Cell cell = cellIterator.next();
                CellType type = CellType.forInt(cell.getCellType());

                if(type != CellType.NUMERIC) continue;

                int cellValue = (int)cell.getNumericCellValue();

                ticket.setIdTicketNumbers(cellValue);
                ticket.setTicketNumber(String.valueOf(cellValue));
                ticket.setIdProduct(cell.getColumnIndex()+1);
                ticketList.add(ticket);
            }
        }

        return ticketList;
    }


    public static List<BundleDetails> bundleImportTickets(Path path) throws IOException {

        XSSFSheet sheet = getSheet(path, "BB Ticket Grouping");

        Iterator<Row> rows = sheet.iterator();
        List<BundleDetails> bundleDetailsList = new ArrayList<>();

        while(rows.hasNext()) {

            Row row = rows.next();
            if (row.getRowNum() < 2) continue;

            Iterator<Cell> cellIterator = row.cellIterator();
            BundleDetails bundleDetails = new BundleDetails();
            List<Ticket> ticketList = new ArrayList<>();
            while (cellIterator.hasNext()) {
                Cell cell = cellIterator.next();
                // assuming all are int
                int cellValue = (int)cell.getNumericCellValue();
                if (cell.getColumnIndex() == BUNLDE_INDEX) {
                    bundleDetails.setBundleNumber(String.valueOf(cellValue));
                    continue;
                }

                Ticket ticket = new Ticket();
                ticket.setIdTicket(cellValue);

                ticket.setIdProduct(getBundelDetailsIdProduct(cell.getColumnIndex()));
                ticketList.add(ticket);

            }
            bundleDetails.setTickets(ticketList);
            bundleDetailsList.add(bundleDetails);
        }


        return bundleDetailsList;

    }

    private static int getBundelDetailsIdProduct(int columnIndex) {

        if(columnIndex < 1 ) {
            throw new RuntimeException("Column index cannot be less than 1");
        }

        int PRODUCT_COLUMN_LIMIT = 5;
        // the first 4 columns should be product id car
        if (columnIndex < PRODUCT_COLUMN_LIMIT ) {
            return Ticket.PRODUCT_ID_CAR;
        } else {
            return Ticket.PRODUCT_ID_VACATION;
        }
    }


    public void insertImportedTickets(List<TicketNumbers> importedTickets) {


        // ImportTicketsDataAccess importTicketsDataAccess = new ImportTicketsDataAccess();
        importedTickets.forEach( (ticket) -> {
            importTicketsDataAccess.saveImportTicket(ticket);
        });

    }


    public static void main(String args[]) throws IOException {

        Path path = Paths.get("C:\\Users\\ayerd\\Documents\\Laboral\\EthosApps\\Projects\\Independents\\Raffle-Spring\\raffle-master\\raffle\\datafiles\\Excel_Import.xlsx");
        // Path path = Paths.get("C:\\Users\\ayerd\\Documents\\Laboral\\EthosApps\\Projects\\Independents\\Raffle-Spring\\db-files\\2019 Sweepstakes prep.xlsx");
        // List<TicketNumbers> ticketNumbers = importTickets(path);
        List<BundleDetails> bundleDetailsList = bundleImportTickets(path);
        System.out.println(bundleDetailsList);
    }
}
