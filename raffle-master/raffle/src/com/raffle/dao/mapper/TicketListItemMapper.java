package com.raffle.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.raffle.pojo.TicketListItem;

public class TicketListItemMapper extends SqlMapperBase implements RowMapper<TicketListItem>  
{

	@Override
	public TicketListItem mapRow(ResultSet rs, int rowNumber) throws SQLException {
		
		TicketListItem item = new TicketListItem();
		
		item.setIdTicket(rs.getInt("idTicket"));
		item.setAddress			(rs.getString("address"));
		item.setCity			(rs.getString("city"));
		item.setCreationDate	(rs.getDate("creationDate"));
		item.setEmail			(rs.getString("email"));
		item.setFirstName		(rs.getString("firstName"));
		
		item.setLastName		(rs.getString("lastName"));
		item.setPhone			(rs.getString("phone"));
		item.setPrice			(rs.getBigDecimal("price"));
		item.setProductName		(rs.getString("productName"));
		item.setPurchaseDate	(rs.getDate("purchaseDate"));
		item.setState			(rs.getString("state"));
		item.setTax				(rs.getBigDecimal("tax"));
		item.setTicketNumber	(rs.getString("ticketNumber"));
		item.setTotal			(rs.getBigDecimal("total"));
		item.setZipcode			(rs.getString("zipCode"));
		item.setPaymentType		(rs.getString("paymentType"));
		item.setActive			(rs.getBoolean("active"));		
		item.setCreatedBy		(rs.getString("createdBy"));		
		item.setMemo			(rs.getString("memo"));		
		item.setModifiedBy		(rs.getString("modifiedBy"));
		
		
		item.setIdProduct		(rs.getInt("idProduct"));
		item.setIdContact		(rs.getInt("idContact"));
		
		
		item.setWeekNumber(rs.getInt("weekNumber"));		
		item.setBundleNumber(rs.getString("bundleNumber"));
		
		
		
		item.setIsWinner(rs.getBoolean("IsWinner")); 
		item.setSetWinnerBy( rs.getInt("SetWinnerBy") ); 
		item.setSetWinnerOn( rs.getDate("SetWinnerOn")); 
		item.setSetWinnerDescription  (rs.getString("SetWinnerDescription"));
		
		if(this.hasColumn(rs, "isInBundle"))
		{
			item.setInBundle( rs.getBoolean("isInBundle") );
			item.setBundleNumberFromCatalog(rs.getString("bundleNumberFromCatalog"));
		}
		
		return item;
	}

}
