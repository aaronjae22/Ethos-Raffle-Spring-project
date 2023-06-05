package com.raffle.dao.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.raffle.pojo.User;


public class UserMapper implements RowMapper<User>{	
	@Override
	public User mapRow(ResultSet rs, int rowNum) throws SQLException {
		
		
		User user = new User();
		
		
		user.setIdUser(rs.getInt("idUser"));
		user.setFullName(rs.getString("fullName"));
		user.setUserName(rs.getString("userName"));
		user.setActive(rs.getBoolean("active"));
		
		user.setAdministrator(rs.getBoolean("administrator"));
		
		user.setCreationDate(rs.getDate("creationDate"));
		
		
		return user;
	} 
}

