package com.raffle.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.raffle.view.model.contact.TicketPurchaseItem;

public class TicketPurchaseItemMapper extends SqlMapperBase implements RowMapper<TicketPurchaseItem> {

	@Override
	public TicketPurchaseItem mapRow(ResultSet rs, int rowNumber) throws SQLException {
		
		TicketPurchaseItem item = new TicketPurchaseItem();
		
		
		item.setIdPeriod(rs.getInt("idPeriod"));
		item.setPeriod(rs.getString("period"));
		item.setIdTicket(rs.getInt("idTicket"));
		
		item.setIdContact(rs.getInt("idContact"));
		item.setContact(rs.getString("contact"));
		item.setTicketNumber(rs.getString("ticketNumber"));
		item.setPurchaseDate(rs.getDate("purchaseDate"));
		
		item.setCretedByUserId(rs.getInt("createdByUserId"));		
		item.setCreatedByUser(rs.getString("createdByUser"));
		
		item.setPaymentType(rs.getString("paymentType"));
		item.setProduct(rs.getString("product"));
		item.setBundleNumber(rs.getString("bundleNumber"));
		item.setPrice(rs.getBigDecimal("price"));
		
		
		
		return item;
	}

}
