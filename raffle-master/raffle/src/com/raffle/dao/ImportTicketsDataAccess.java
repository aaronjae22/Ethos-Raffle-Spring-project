package com.raffle.dao;

import com.raffle.pojo.BundleDetails;
import com.raffle.pojo.Ticket;
import com.raffle.pojo.TicketNumbers;
import com.raffle.view.model.BundleTickets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

@Controller
public class ImportTicketsDataAccess extends BaseDataAccess {

    @Autowired
    PeriodDataAccess periodDataAccess;

    public void saveImportTicket(TicketNumbers ticket)
    {
        String INSERT_SQL = "insert ignore into ticket_numbers (idPeriod, ticket_number, idProduct)" +
                "values(?, ?, ?)";

        int currentPeriod = periodDataAccess.getCurrentPeriod(); // active = 1

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(
                new PreparedStatementCreator() {
                    public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                        PreparedStatement ps = connection.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS);

                        ps.setInt(1, currentPeriod);
                        ps.setString(2, ticket.getTicketNumber());
                        ps.setInt(3, ticket.getIdProduct());


                        return ps;

                    }
                },
                keyHolder
        );

    }


    public void saveImportBundleTicket(BundleDetails ticket) {

        System.out.println("\nTicket: " + ticket.getTickets());
        System.out.println("Ticket ID Product " + ticket.getIdProduct() + "\n");


        String INSERT_SQL = "insert into bundle_details (idPeriod, bundleNumber, ticketNumber, idProduct)" +
                "values(?, ?, ?, ?)";

        int currentPeriod = periodDataAccess.getCurrentPeriod(); // active = 1

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(
                new PreparedStatementCreator() {
                    public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {

                        PreparedStatement ps = connection.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS);

                        ps.setInt(1, currentPeriod);
                        ps.setString(2, ticket.getBundleNumber());
                        ps.setString(3, ticket.getTicketNumber());
                        ps.setInt(4, ticket.getIdProduct());


                        return ps;

                    }
                },
                keyHolder
        );

    }

}
