package com.raffle.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.raffle.pojo.Contact;

public class ContactMapper implements RowMapper<Contact>{

	@Override
	public Contact mapRow(ResultSet rs, int rowNum) throws SQLException {

		Contact contact = new Contact();
		

		contact.setIdContact(rs.getInt("idContact"));
		contact.setFirstName(rs.getString("firstName"));
		
		contact.setLastName(rs.getString("lastName"));
		contact.setEmail(rs.getString("email"));
		contact.setPhone(rs.getString("phone"));
		contact.setAddress(rs.getString("address"));
		contact.setCity(rs.getString("city"));
		contact.setState(rs.getString("state"));
		contact.setZipCode(rs.getString("zipcode"));		
		
		return contact;
	}

}
