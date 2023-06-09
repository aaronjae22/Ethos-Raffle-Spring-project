package com.raffle;

import com.raffle.pojo.Ticket;
import com.raffle.pojo.TicketNumbers;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ImportTicketsHelper {
    public static void importTickets(Path path) throws IOException {

        String excelFilePath = String.valueOf(path);
        FileInputStream inputStream = new FileInputStream(excelFilePath);
        XSSFWorkbook workbook = new XSSFWorkbook(inputStream);

        XSSFSheet sheet = workbook.getSheet("Tickets");

        Iterator<Row> rows = sheet.iterator();
        // List<Ticket> ticketList = new ArrayList<>();
        List<TicketNumbers> ticketList = new ArrayList<>();

        while(rows.hasNext()) {
            Row row = rows.next();
            if(row.getRowNum() < 2) continue;

            Iterator<Cell> cellIterator  = row.cellIterator();
            // Ticket ticket = new Ticket();
            TicketNumbers ticket = new TicketNumbers();

            while(cellIterator.hasNext()) {
                Cell cell = cellIterator.next();
                CellType type = CellType.forInt(cell.getCellType());

                if(type != CellType.NUMERIC) continue;

                if(cell.getColumnIndex() == 0 ) {
                    ticket.setIdTicketNumbers((int)cell.getNumericCellValue());
                }

                if(cell.getColumnIndex() == 1 ) {
                    ticket.setIdProduct((int)cell.getNumericCellValue());
                }

                if(cell.getColumnIndex() == 2 ) {
                    ticket.setIdPeriod((int)cell.getNumericCellValue());
                }
            }
            ticketList.add(ticket);
        }
        // for(Ticket ticket : ticketList) {
        for(TicketNumbers ticket : ticketList) {
            System.out.println(ticket.getIdTicketNumbers());
            System.out.println(ticket.getIdProduct());
            System.out.println(ticket.getIdPeriod());
            System.out.println(ticket);
            System.out.println();
        }
    }

    public static void main(String args[]) throws IOException {

        Path path = Paths.get("C:\\Users\\ayerd\\Documents\\Laboral\\EthosApps\\Projects\\Independents\\Raffle-Spring\\raffle-master\\raffle\\datafiles\\Excel_Import.xlsx");
        importTickets(path);
    }
}
