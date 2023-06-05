package com.raffle.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.raffle.pojo.ContactCallDetail;

public class ContactCallDetailMapper extends SqlMapperBase implements RowMapper<ContactCallDetail>
{
	@Override
	public ContactCallDetail mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		
		ContactCallDetail call = new ContactCallDetail();
					
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
		
				
		call.setEmail(rs.getString("email"));
		call.setPhone(rs.getString("phone"));
		call.setAddress(rs.getString("address"));
		call.setCity(rs.getString("city"));
		call.setState(rs.getString("state"));
		call.setZipcode(rs.getString("zipcode"));
		
		
		return call;
	}
	

}
