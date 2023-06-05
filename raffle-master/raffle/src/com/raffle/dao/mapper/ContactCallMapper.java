package com.raffle.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.raffle.pojo.ContactCall;

public class ContactCallMapper extends SqlMapperBase implements RowMapper<ContactCall>{

	@Override
	public ContactCall mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		
		ContactCall call = new ContactCall();
					
		call.setIdContact_call(rs.getInt("idContact_call"));
		
		call.setIdContact(rs.getInt("idContact"));
		
		if(this.hasColumn(rs, "contact"))
			call.setContact(rs.getString("contact"));
		
		
		call.setCallDate(rs.getDate("callDate"));
		call.setCreatedOn(rs.getTimestamp("createdOn"));
		
		call.setCreatedByUserId(rs.getInt("createdByUserId"));
		if(this.hasColumn(rs, "createdByUser"))
			call.setCreatedByUser(rs.getString("createdByUser"));
		
		call.setMemo(rs.getString("memo"));
		
		
		call.setSpokenWithContact(rs.getBoolean("spokenWithContact"));
		
		
		call.setFollowUpCallId(rs.getInt("followUpCallId"));
		if(this.hasColumn(rs, "followUpCall"))
			call.setFollowUpCall(rs.getString("followUpCall"));
		
		return call;
	}
	
	

}
