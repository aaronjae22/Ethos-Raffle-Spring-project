package com.raffle.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.raffle.pojo.FollowUpCall;

public class FollowUpCallMapper 
	 implements RowMapper<FollowUpCall>  
	 {

	 	@Override
	 	public FollowUpCall mapRow(ResultSet rs, int rowNumber) throws SQLException {
	 		
	 		FollowUpCall item = new FollowUpCall();
	 	
	 		item.setIdFollowUp_Call(rs.getInt("idFollowUp_Call"));
	 		item.setDescription(rs.getString("Description"));
	 		
	 		
	 		return item;
	 	}
	 	
}
