package com.raffle.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.raffle.pojo.TicketNumbers;


public class TicketNumbersMapper implements RowMapper<TicketNumbers>{	
	@Override
	public TicketNumbers mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		
		TicketNumbers ticket = new TicketNumbers();
		
		
		ticket.setIdPeriod(rs.getInt("idPeriod"));
		ticket.setIdProduct(rs.getInt("idProduct"));
		ticket.setTicketNumber(rs.getString("ticket_number"));
		ticket.setIdTicketNumbers(rs.getInt("idTicket_numbers"));
		
		
		return ticket;
	} 
}

