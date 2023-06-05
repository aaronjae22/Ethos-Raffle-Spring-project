package com.raffle.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.raffle.view.model.EditTicketValidationInfo;

public class EditTicketValidationInfoMapper  implements RowMapper<EditTicketValidationInfo>  
{

	@Override
	public EditTicketValidationInfo mapRow(ResultSet rs, int rowNum) throws SQLException 
	{
		
		
		EditTicketValidationInfo info = new EditTicketValidationInfo();
		
		info.setTicketNumber(rs.getString("ticketNumber"));
		info.setIdProduct(rs.getInt("idProduct"));
		info.setSold(rs.getBoolean("isSold"));
		info.setBundleNumber(rs.getString("bundleNumber"));
		info.setBroken(rs.getBoolean("isBroken"));
		info.setBrokenByUserId(rs.getInt("brokenByUserId"));
		info.setBrokenOn(rs.getDate("brokenOn"));
		
		return info;
	}

}
